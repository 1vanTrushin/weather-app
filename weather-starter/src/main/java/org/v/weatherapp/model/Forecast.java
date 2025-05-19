package org.v.weatherapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Forecast {
    public List<ForecastDay> forecastday;

}