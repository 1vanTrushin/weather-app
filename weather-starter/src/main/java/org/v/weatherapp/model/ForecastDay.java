package org.v.weatherapp.model;

import lombok.Data;

@Data
public class ForecastDay {
    private String date;
    private Day day;
}
