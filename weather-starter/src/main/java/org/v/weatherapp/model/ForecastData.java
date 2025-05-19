package org.v.weatherapp.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
public class ForecastData {
    private String city;
    private List<WeatherData> dailyForecasts;

}
