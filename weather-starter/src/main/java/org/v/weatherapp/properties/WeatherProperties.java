package org.v.weatherapp.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;


@ConfigurationProperties(prefix = "weather")
@Data
@Validated
public class WeatherProperties {
    @NotEmpty
    private String apiUrl;
    @NotEmpty
    private String apiKey;
    private boolean useMock;
    private Duration cacheTtl = Duration.ofMinutes(10);

}
