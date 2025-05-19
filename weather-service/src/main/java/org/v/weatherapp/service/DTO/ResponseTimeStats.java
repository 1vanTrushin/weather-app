package org.v.weatherapp.service.DTO;

import lombok.Getter;

@Getter
public class ResponseTimeStats {
    private final long count;
    private final double totalTimeMillis;
    private final double maxTimeMillis;
    private final double averageTimeMillis;

    public ResponseTimeStats(long count, double totalTimeMillis, double maxTimeMillis, double averageTimeMillis) {
        this.count = count;
        this.totalTimeMillis = totalTimeMillis;
        this.maxTimeMillis = maxTimeMillis;
        this.averageTimeMillis = averageTimeMillis;
    }
}
