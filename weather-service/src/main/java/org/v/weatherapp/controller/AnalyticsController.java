package org.v.weatherapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.v.weatherapp.service.AnalyticsService;
import org.v.weatherapp.service.DTO.RequestFrequencyStats;
import org.v.weatherapp.service.DTO.ResponseTimeStats;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/popular-cities")
    public Map<String, Long> getPopularCities(@RequestParam(defaultValue = "10") int limit) {
        return analyticsService.getPopularCities(limit);
    }

    @GetMapping("/response-times")
    public ResponseTimeStats getResponseTimeStatistics() {
        return analyticsService.getResponseTimeStatistics();
    }

    @GetMapping("/requests")
    public RequestFrequencyStats getRequestFrequencyStatistics() {
        return analyticsService.getRequestFrequencyStatistics();
    }


}
