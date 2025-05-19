package org.v.weatherapp.client;

import org.v.weatherapp.model.WeatherData;

public interface WeatherApiClient {
    WeatherData getCurrentWeather(String city);
    WeatherData getForecastWeather(String city, String Days);
}
