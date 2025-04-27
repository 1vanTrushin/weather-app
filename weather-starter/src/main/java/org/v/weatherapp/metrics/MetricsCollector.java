package org.v.weatherapp.metrics;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

@Component
public class MetricsCollector {

    private final Map<String, MethodMetrics> methodMetrics = new ConcurrentHashMap<>();

    private static class MethodMetrics {
        private final LongAdder callCount = new LongAdder();
        private final LongAdder totalExecutionTime = new LongAdder();
        private final AtomicLong maxExecutionTime = new AtomicLong(0);
        private final List<Long> executionTimes = new ArrayList<>();

        public void record(long executionTime) {
            callCount.increment();
            totalExecutionTime.add(executionTime);
            maxExecutionTime.updateAndGet(currentMax -> Math.max(currentMax, executionTime));
            executionTimes.add(executionTime);
        }

        public long getCallCount() {
            return callCount.sum();
        }

        public double getAverageExecutionTime() {
            long count = callCount.sum();
            return count == 0 ? 0 : (double) totalExecutionTime.sum() / count;
        }

        public long getMaxExecutionTime() {
            return maxExecutionTime.get();
        }

        public List<Long> getExecutionTimes() {
            return new ArrayList<>(executionTimes);
        }
    }

    private long calculatePercentile(List<Long> times, double percentile) {
        if (times == null || times.isEmpty()) {
            return 0;
        }
        List<Long> sortedTimes = times.stream().sorted().collect(Collectors.toList());
        int index = (int) Math.ceil(percentile / 100.0 * sortedTimes.size()) - 1;
        return sortedTimes.get(Math.max(0, index));
    }

    public void recordExecutionTime(String methodSignature, long executionTime) {
        MethodMetrics metrics = methodMetrics.computeIfAbsent(methodSignature, k -> new MethodMetrics());
        metrics.record(executionTime);
    }

    public Map<String, Map<String, Object>> getMetrics() {
        return methodMetrics.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            MethodMetrics metrics = entry.getValue();
                            List<Long> executionTimes = metrics.getExecutionTimes();
                            return Map.of(
                                    "callCount", metrics.getCallCount(),
                                    "averageExecutionTime", metrics.getAverageExecutionTime(),
                                    "maxExecutionTime", metrics.getMaxExecutionTime(),
                                    "p95", calculatePercentile(executionTimes, 95.0),
                                    "p99", calculatePercentile(executionTimes, 99.0)
                            );
                        }
                ));
    }

}
