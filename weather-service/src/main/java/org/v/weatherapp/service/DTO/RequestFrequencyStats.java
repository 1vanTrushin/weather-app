package org.v.weatherapp.service.DTO;

import lombok.Getter;

import java.util.Map;

@Getter
public class RequestFrequencyStats {
    private final double totalRequests;
    private final Map<String, Long> requestsByType;

    public RequestFrequencyStats(double totalRequests, Map<String, Long> requestsByType) {
        this.totalRequests = totalRequests;
        this.requestsByType = requestsByType;
    }
}
