package ai.ameron.sidecar.integration.model.http;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter(value = AccessLevel.PACKAGE)
@ConditionalOnProperty(
    value="app.model-service.adapter.backend",
    havingValue = "http")
@Configuration
public class HttpPredictionServiceConfiguration {
  @Value("${app.model-service.adapter.backend.http.url}")
  private String modelServiceUrl;
}
