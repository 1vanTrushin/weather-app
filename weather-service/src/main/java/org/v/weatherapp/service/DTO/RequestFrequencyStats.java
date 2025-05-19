package org.v.weatherapp.service.DTO;

import lombok.Getter;

import java.util.Map;

@Getter
public class RequestFrequencyStats {
    private final double totalRequests;
    private final Map<String, Long> requestsByForecastType;

    public RequestFrequencyStats(double totalRequests, Map<String, Long> requestsByForecastType) {
        this.totalRequests = totalRequests;
        this.requestsByForecastType = requestsByForecastType;
    }
}
