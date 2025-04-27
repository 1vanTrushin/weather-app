package org.v.weatherapp.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.v.weatherapp.model.Current;
import org.v.weatherapp.model.ForecastData;
import org.v.weatherapp.model.Location;
import org.v.weatherapp.model.WeatherData;


@ConditionalOnProperty(name = "weather.use-mock", havingValue = "true")
public class MockWeatherApiClient implements WeatherApiClient {
    @Override
    public WeatherData getCurrentWeather(String city) {

        Current current = new Current();
        current.setTemp_c(15.0);
        Location location = new Location();
        location.setName("Moscow");

        WeatherData weatherData = new WeatherData();
        weatherData.setCurrent(current);
        weatherData.setLocation(location);

        return weatherData;
    }

//    @Override
//    public ForecastData getForecast(String city) {
//        return new ForecastData();
//    }
}
