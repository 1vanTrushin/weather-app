package org.v.weatherapp.model;

import lombok.Data;

@Data
public class WeatherData {

    private Location location;
    private Current current;
    private Forecast forecast;

}
