package org.v.weatherapp.client;

import org.v.weatherapp.model.ForecastData;
import org.v.weatherapp.model.WeatherData;

public interface WeatherApiClient {
    WeatherData getCurrentWeather(String city);

//    ForecastData getForecast(String city);
}
