package org.v.weatherapp.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.v.weatherapp.metrics.TrackMetric;

@Service
public class MockService {

    @TrackMetric(value = "MyMetric")
    @SneakyThrows
    public String test() {
        Thread.sleep(1111);
        return "MyMetric";
    }
}
