package org.v.weatherapp.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.v.weatherapp.metrics.TrackMetric;
import org.v.weatherapp.model.WeatherData;
import org.v.weatherapp.service.MockService;
import org.v.weatherapp.service.WeatherService;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final MockService mockService;

    @GetMapping("/api/weather/{city}")
    @TrackMetric(value = "getWeatherByCityMetric")
    public WeatherData getWeatherByCity(@PathVariable String city) {
        return weatherService.getWeatherByCity(city);
    }

}
