package org.v.weatherapp.autoconfigure;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.v.weatherapp.client.impl.WeatherApiClientImpl;
import org.v.weatherapp.client.impl.MockWeatherApiClientImpl;
import org.v.weatherapp.metrics.TrackMetricBeanPostProcessor;
import org.v.weatherapp.properties.WeatherProperties;

@Configuration
@EnableConfigurationProperties(WeatherProperties.class)
//вообще отключить конфигурацию
public class WeatherAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public org.v.weatherapp.client.WeatherApiClient weatherApiClient(WeatherProperties properties, WebClient.Builder webClientBuilder) {
        return properties.isUseMock() ?
                new MockWeatherApiClientImpl() :
                new WeatherApiClientImpl(webClientBuilder, properties);
    }

    @Bean
    public static TrackMetricBeanPostProcessor trackMetricBeanPostProcessor(MeterRegistry meterRegistry) {
        return new TrackMetricBeanPostProcessor(meterRegistry);
    }
}
