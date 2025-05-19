package org.v.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.v.weatherapp.client.WeatherApiClient;
import org.v.weatherapp.model.Forecast;
import org.v.weatherapp.model.WeatherData;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherApiClient weatherApiClient;

    public WeatherData getWeatherByCity(String city, String days) {
        return days.isEmpty() ? weatherApiClient.getCurrentWeather(city)
                : weatherApiClient.getForecastWeather(city, days);
    }

}
