package org.v.weatherapp.client.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.v.weatherapp.client.WeatherApiClient;
import org.v.weatherapp.model.Current;
import org.v.weatherapp.model.Location;
import org.v.weatherapp.model.WeatherData;


@ConditionalOnProperty(name = "weather.use-mock", havingValue = "true")
public class MockWeatherApiClient implements WeatherApiClient {
    @Override
    public WeatherData getCurrentWeather(String city) {

        //вынести в статичный объект
        Current current = new Current();
        current.setTempC(15.0);
        Location location = new Location();
        location.setName("Moscow");
        //вынести в статичный объект

        WeatherData weatherData = new WeatherData();
        weatherData.setCurrent(current);
        weatherData.setLocation(location);
        //вынести в статичный объект

        return weatherData;
    }

//    @Override
//    public ForecastData getForecast(String city) {
//        return new ForecastData();
//    }
}
