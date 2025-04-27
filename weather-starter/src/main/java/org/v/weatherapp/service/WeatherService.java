package org.v.weatherapp.service;

import org.v.weatherapp.client.WeatherApiClient;
import org.v.weatherapp.metrics.TrackMetric;
import org.v.weatherapp.model.WeatherData;

public class WeatherService {

    private final WeatherApiClient weatherApiClient;


    public WeatherService(WeatherApiClient weatherApiClient) {
        this.weatherApiClient = weatherApiClient;
    }

    @TrackMetric(value = "get_weather", description = "Fetch current data")
    public WeatherData getWeather(String city) {
        return weatherApiClient.getCurrentWeather(city);
    }

}
