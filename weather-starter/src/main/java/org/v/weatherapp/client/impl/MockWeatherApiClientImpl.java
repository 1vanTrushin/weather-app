package org.v.weatherapp.client.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.v.weatherapp.client.WeatherApiClient;
import org.v.weatherapp.model.*;
import reactor.core.publisher.Mono;

import java.util.List;


@ConditionalOnProperty(name = "weather.use-mock", havingValue = "true")
public class MockWeatherApiClientImpl implements WeatherApiClient {
    @Override
    public Mono<WeatherData> getCurrentWeather(String city) {
        return Mono.just(WeatherDataFactory.createWeatherData());
    }


    @Override
    public Mono<WeatherData> getForecastWeather(String city, String Days) {
        return Mono.just(WeatherDataFactory.createForecastWeatherData());
    }


    static class WeatherDataFactory {
        private static final double DEFAULT_TEMP = 15.0;
        private static final String DEFAULT_CITY = "Moscow";

        public static Current createCurrent() {
            Current current = new Current();
            current.setTempC(DEFAULT_TEMP);
            return current;
        }

        public static Location createLocation() {
            Location location = new Location();
            location.setName(DEFAULT_CITY);
            return location;
        }

        public static Forecast createForecast() {
            Forecast forecast = new Forecast();
            ForecastDay forecastDay = new ForecastDay();
            Day day = new Day();
            day.setMaxtempC(15.1);
            day.setMaxtempC(1.1);
            forecastDay.setDate("2025-05-20");
            forecastDay.setDay(day);
            forecast.setForecastday(List.of(forecastDay));
            return forecast;
        }

        public static WeatherData createWeatherData() {
            WeatherData weatherData = new WeatherData();
            weatherData.setCurrent(createCurrent());
            weatherData.setLocation(createLocation());
            return weatherData;
        }

        public static WeatherData createForecastWeatherData() {
            WeatherData weatherData = createWeatherData();
            weatherData.setForecast(createForecast());
            return weatherData;
        }

    }
}
