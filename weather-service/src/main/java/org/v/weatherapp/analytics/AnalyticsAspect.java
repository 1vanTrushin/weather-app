package org.v.weatherapp.analytics;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.v.weatherapp.service.AnalyticsService;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class AnalyticsAspect {
    private final AnalyticsService analyticsService;

    public AnalyticsAspect(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Pointcut("execution(* org.v.weatherapp.service.WeatherService.getWeatherByCity(String)) && args(city)")
    public void weatherServiceGetWeatherByCityPointcut(String city) {}

    @Around(value = "weatherServiceGetWeatherByCityPointcut(city)", argNames = "joinPoint,city")
    public Object measureExternalApiResponseTime(ProceedingJoinPoint joinPoint, String city) throws Throwable {
        long startTime = System.nanoTime();
        Object result;
        try {
            analyticsService.recordWeatherApiRequest(city);
            result = joinPoint.proceed();
        } finally {
            long durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            analyticsService.recordExternalApiResponseTime(durationMillis);
        }
        return result;
    }
}
