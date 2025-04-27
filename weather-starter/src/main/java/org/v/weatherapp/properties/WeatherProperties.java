package org.v.weatherapp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


@ConfigurationProperties(prefix = "weather")
@Data
public class WeatherProperties {
    private String apiUrl;
    private String apiKey;
    private boolean useMock;
    private Duration cacheTtl = Duration.ofMinutes(10);

}
