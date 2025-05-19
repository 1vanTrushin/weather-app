package org.v.weatherapp.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Location {
    private String name;
    private String region;
    private String country;
    private Double lat;
    private Double lon;
    private String tzId;
    private Long localtimeEpoch;
    private String localtime;
}
