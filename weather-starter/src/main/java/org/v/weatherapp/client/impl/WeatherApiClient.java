package org.v.weatherapp.client.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.reactive.function.client.WebClient;
import org.v.weatherapp.model.WeatherData;
import org.v.weatherapp.properties.WeatherProperties;


@ConditionalOnProperty(name = "weather.use-mock", havingValue = "false", matchIfMissing = true)
public class WeatherApiClient implements org.v.weatherapp.client.WeatherApiClient {

    private final WebClient webClient;
    private final WeatherProperties properties;

    public WeatherApiClient(WebClient.Builder builder, WeatherProperties properties) {
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
//    @Override
//    public ForecastData getForecast(String city) {
//        return new ForecastData(city, Collections.singletonList(getCurrentWeather(city)));
//    }
}
