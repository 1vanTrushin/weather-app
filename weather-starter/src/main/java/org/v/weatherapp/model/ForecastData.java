package org.v.weatherapp.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ForecastData {
    private String city;
    private List<WeatherData> dailyForecasts;

    public ForecastData(String city, List<String> strings) {
    }
}
