package com.nriagudubem.weatherbot.service;

import com.nriagudubem.weatherbot.config.EmailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class EmailService {

    @Value("${api.location}")
    String location;
    @Autowired
    EmailConfig emailConfig;
    @Autowired
    private WeatherBotService weatherService;

    @Scheduled(cron = "0 */5 * ? * *")
    public void sendMail() {
        JavaMailSender sender = emailConfig.configure();
        String forecast = weatherService.getWeatherForeCast(location);
        SimpleMailMessage message = new SimpleMailMessage();
        String to = "nriagudubem@yahoo.com";
        message.setTo(to);
        message.setSubject("WEATHER FOR TODAY " + LocalDate.now());
        message.setText(forecast);
        message.setFrom("WeatherBot");

        try {
            sender.send(message);
            log.info("weather report has been sent to " + to);
        } catch (MailException ex) {
            log.error("An exception has occurred " + ex.getLocalizedMessage());
        }
    }
}
