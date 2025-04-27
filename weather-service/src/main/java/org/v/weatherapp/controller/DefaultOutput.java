package org.v.weatherapp.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.v.weatherapp.service.WeatherService;

@RestController
public class DefaultOutput {

    private final WeatherService weatherService;

    public DefaultOutput(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


    @GetMapping("/api/weather/{city}")
    public String index(@PathVariable String city) {
        return weatherService.getWeather(city).toString();
    }

}
