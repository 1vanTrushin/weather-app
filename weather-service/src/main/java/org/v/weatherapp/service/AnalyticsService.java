package org.v.weatherapp.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import org.v.weatherapp.service.DTO.RequestFrequencyStats;
import org.v.weatherapp.service.DTO.ResponseTimeStats;

import java.time.Duration;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final Map<String, AtomicLong> cityRequestCounts = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> forecastTypeCounts = new ConcurrentHashMap<>();

    private final Counter totalRequestsCounter;
    private final Timer externalApiTimer;

    public AnalyticsService(MeterRegistry meterRegistry) {

        this.totalRequestsCounter = Counter.builder("weather.api.requests.total")
                .description("Total number of requests to the weather API")
                .register(meterRegistry);

        this.externalApiTimer = Timer.builder("weather.external.api.response.time")
                .description("Response time of the external weather API")
                .publishPercentiles(0.5, 0.95, 0.99)
                .sla(Duration.ofMillis(500), Duration.ofSeconds(1))
                .register(meterRegistry);
    }

    public void recordWeatherApiRequest(String city) {
        totalRequestsCounter.increment();
        cityRequestCounts.computeIfAbsent(city, k -> new AtomicLong(0)).incrementAndGet();
//        forecastTypeCounts.computeIfAbsent(forecastType, k -> new AtomicLong(0)).incrementAndGet();
    }

    public void recordExternalApiResponseTime(long timeMillis) {
        externalApiTimer.record(timeMillis, TimeUnit.MILLISECONDS);
    }

    public Map<String, Long> getPopularCities(int limit) {
        return cityRequestCounts.entrySet().stream()
                .sorted(Map.Entry.<String, AtomicLong>comparingByValue(Comparator.comparingLong(AtomicLong::get)).reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().get(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public ResponseTimeStats getResponseTimeStatistics() {
        if (externalApiTimer == null) {
            return new ResponseTimeStats(0, 0, 0, 0);
        }
        return new ResponseTimeStats(
                externalApiTimer.count(),
                externalApiTimer.totalTime(TimeUnit.MILLISECONDS),
                externalApiTimer.max(TimeUnit.MILLISECONDS),
                externalApiTimer.mean(TimeUnit.MILLISECONDS)
        );
    }

    public RequestFrequencyStats getRequestFrequencyStatistics() {
        Map<String, Long> forecastCounts = forecastTypeCounts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));

        return new RequestFrequencyStats(totalRequestsCounter.count(), forecastCounts);
    }


}
