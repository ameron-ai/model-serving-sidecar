package ai.ameron.sidecar.integration.event.kafka;

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
    havingValue = "kafka")
@Configuration
public class KafkaPredictionEventConsumerConfiguration {
  @Value("${app.prediction-event.consumer.kafka.topic}")
  private String topic;
}
