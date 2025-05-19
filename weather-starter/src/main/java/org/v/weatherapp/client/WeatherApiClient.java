package org.v.weatherapp.client;

import org.v.weatherapp.model.WeatherData;
import reactor.core.publisher.Mono;

public interface WeatherApiClient {
    Mono<WeatherData> getCurrentWeather(String city);
    Mono<WeatherData> getForecastWeather(String city, String Days);
}
