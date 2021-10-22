package ai.ameron.sidecar.integration.reporter.stream;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter(value = AccessLevel.PACKAGE)
@ConditionalOnProperty(
    value="app.prediction-event.consumer",
    havingValue = "stream")
@Configuration
public class StreamPredictionEventConsumerConfiguration {
  @Value("${app.prediction-event.consumer.binding.name}")
  private String bindingName;
}
