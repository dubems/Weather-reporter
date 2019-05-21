package com.nriagudubem.weatherbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeatherBotService {

    @Autowired
    private HttpUtilityService httpUtilityService;

    public String getWeatherForeCast(String location) {
      return httpUtilityService.getRequest(location);
    }
}
