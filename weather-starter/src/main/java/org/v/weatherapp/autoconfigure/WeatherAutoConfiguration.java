package org.v.weatherapp.autoconfigure;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.v.weatherapp.client.DefaultWeatherApiClient;
import org.v.weatherapp.client.MockWeatherApiClient;
import org.v.weatherapp.client.WeatherApiClient;
import org.v.weatherapp.metrics.MetricsCollector;
import org.v.weatherapp.metrics.WeatherMetricsBeanFactoryPostProcessor;
import org.v.weatherapp.metrics.WeatherMetricsBeanPostProcessor;
import org.v.weatherapp.properties.WeatherProperties;
import org.v.weatherapp.service.WeatherService;

@Configuration
@EnableConfigurationProperties(WeatherProperties.class)
public class WeatherAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WeatherApiClient weatherApiClient(WeatherProperties properties, WebClient.Builder webClientBuilder) {
            return properties.isUseMock() ?
                    new MockWeatherApiClient() :
                    new DefaultWeatherApiClient(webClientBuilder, properties);
        }

    @Bean
    @ConditionalOnMissingBean

    public WeatherService weatherService(WeatherApiClient weatherApiClient) {
        return new WeatherService(weatherApiClient);
    }

//    @Bean
//    public static WeatherMetricsBeanFactoryPostProcessor weatherMetricsBeanFactoryPostProcessor(MetricsCollector metricsCollector) {
//        return new WeatherMetricsBeanFactoryPostProcessor(metricsCollector);
//    }
//    @Bean
//    public static WeatherMetricsBeanPostProcessor weatherMetricsBeanFactoryPostProcessor(MetricsCollector metricsCollector) {
//        return new WeatherMetricsBeanPostProcessor(metricsCollector);
//    }

}
