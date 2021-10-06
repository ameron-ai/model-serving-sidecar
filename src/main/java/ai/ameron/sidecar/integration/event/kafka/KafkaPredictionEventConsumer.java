package ai.ameron.sidecar.integration.event.kafka;

import ai.ameron.sidecar.core.event.PredictionEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(
    value="app.prediction-event.consumer",
    havingValue = "kafka")
@Service
public class KafkaPredictionEventConsumer {
  private KafkaPredictionEventConsumerConfiguration configuration;
  private KafkaTemplate kafka;

  @EventListener
  public void handlePredictionEvent(PredictionEvent event) {
    log.debug("Sending PredictionEvent: " + event.asJson());
    kafka.send(configuration.getTopic(), event);
    log.debug("PredictionEvent sent");
  }
}
