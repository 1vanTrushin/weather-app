package org.v.weatherapp.client.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.reactive.function.client.WebClient;
import org.v.weatherapp.client.WeatherApiClient;
import org.v.weatherapp.model.Forecast;
import org.v.weatherapp.model.WeatherData;
import org.v.weatherapp.properties.WeatherProperties;


@ConditionalOnProperty(name = "weather.use-mock", havingValue = "false", matchIfMissing = true)
public class WeatherApiClientImpl implements WeatherApiClient {

    private final WebClient webClient;
    private final WeatherProperties properties;

    public WeatherApiClientImpl(WebClient.Builder builder, WeatherProperties properties) {
        this.properties = properties;
        this.webClient = builder.baseUrl(properties.getApiUrl()).build();
    }

    @Override
    public WeatherData getCurrentWeather(String city) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/v1/current.json")
                        .queryParam("q", city)
                        .queryParam("key", properties.getApiKey())
                        .queryParam("aqi", "no")
                        .build())
                    .retrieve()
                    .bodyToMono(WeatherData.class)
                    .block();
        }

    @Override
    public WeatherData getForecastWeather(String city, String days) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast.json")
                        .queryParam("key", properties.getApiKey())
                        .queryParam("q", city)
                        .queryParam("days", days)
                        .queryParam("aqi", "no")
                        .queryParam("alerts", "no")
                        .build())
                    .retrieve()
                    .bodyToMono(WeatherData.class)
                    .block();
        }



}
