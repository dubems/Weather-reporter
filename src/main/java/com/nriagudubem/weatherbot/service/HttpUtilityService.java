package com.nriagudubem.weatherbot.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HttpUtilityService {

    @Autowired
    MeterRegistry registry;

    @Value("${api.key}")
    String apiKey;

    @Value("${api.host_url}")
    String hostUrl;

    public String getRequest(String location) {
        registry.counter("weather_requests");
        try {
            URI formattedUrl = formatUrl(location);

            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet(formattedUrl);
            CloseableHttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.OK.value()) {
                registry.counter("weather_request_success");
                return entity == null ? null : EntityUtils.toString(entity);
            }
            throw new Exception();
        } catch (IOException ex) {
            log.error("exception has occured because " + ex.getMessage());
        } catch (URISyntaxException ex) {
            log.error("another exception " + ex.getMessage());
        } catch (Exception ex) {
            log.error("Cannot get current weather " + ex.getMessage());
        }

        return null;
    }

    private URI formatUrl(String location) throws URISyntaxException {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("q", location));
        parameters.add(new BasicNameValuePair("APPID", apiKey));

        return new URIBuilder()
                .setScheme("http")
                .setHost(hostUrl)
                .setPath("/data/2.5/weather")
                .setParameters(parameters).build();
    }
}
