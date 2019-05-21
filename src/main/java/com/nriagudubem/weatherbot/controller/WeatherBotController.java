package com.nriagudubem.weatherbot.controller;

import com.nriagudubem.weatherbot.service.WeatherBotService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class WeatherBotController {

    @Autowired
    private WeatherBotService weatherBotService;

    @Autowired
    MeterRegistry registry;

    @GetMapping("/today-forecast")
    public ResponseEntity<?> getForecastForToday(@RequestParam String location){
        return new ResponseEntity<Object>(weatherBotService.getWeatherForeCast(location),HttpStatus.OK);
    }
}
