package org.v.weatherapp.client.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.v.weatherapp.client.WeatherApiClient;
import org.v.weatherapp.model.WeatherData;
import org.v.weatherapp.properties.WeatherProperties;
import org.v.weatherapp.util.WeatherApiException;
import reactor.core.publisher.Mono;


@ConditionalOnProperty(name = "weather.use-mock", havingValue = "false", matchIfMissing = true)
public class WeatherApiClientImpl implements WeatherApiClient {

    private final WebClient webClient;
    private final WeatherProperties properties;

    public WeatherApiClientImpl(WebClient.Builder builder, WeatherProperties properties) {
        this.properties = properties;
        this.webClient = builder.baseUrl(properties.getApiUrl()).build();
    }


    @Override
    public Mono<WeatherData> getCurrentWeather(String city) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/v1/current.json")
                        .queryParam("q", city)
                        .queryParam("key", properties.getApiKey())
                        .queryParam("aqi", "no")
                        .build())
                .exchangeToMono(WeatherApiClientImpl::apply)
                .onErrorResume(Exception.class, ex -> {
                    System.err.println("Generic error: " + ex.getMessage());
                    return Mono.error(new WeatherApiException("Generic error: " + ex.getMessage()));
                });
    }

    @Override
    public Mono<WeatherData> getForecastWeather(String city, String days) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast.json")
                        .queryParam("key", properties.getApiKey())
                        .queryParam("q", city)
                        .queryParam("days", days)
                        .queryParam("aqi", "no")
                        .queryParam("alerts", "no")
                        .build())
                .exchangeToMono(WeatherApiClientImpl::apply)
                .onErrorResume(Exception.class, ex -> {
                    System.err.println("Generic error: " + ex.getMessage());
                    return Mono.error(new WeatherApiException("Generic error: " + ex.getMessage()));
                });

    }

    private static Mono<WeatherData> apply(ClientResponse clientResponse) {
        if (clientResponse.statusCode().isError()) {
            if (clientResponse.statusCode().equals(HttpStatus.BAD_REQUEST)) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            System.err.println("Bad request: " + errorBody);
                            //todo change to error message
                            return Mono.empty();
                        });
            } else {
                return Mono.error(new WeatherApiException("Error from weather API: " + clientResponse.statusCode()));
            }
        } else {
            return clientResponse.bodyToMono(WeatherData.class);
        }
    }

}
