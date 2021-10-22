package ai.ameron.sidecar.integration.reporter.stream;

import ai.ameron.sidecar.core.event.PredictionEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(
    value="app.prediction-event.consumer",
    havingValue = "stream")
@Service
public class StreamPredictionEventConsumer {
  private StreamPredictionEventConsumerConfiguration configuration;
  private StreamBridge streamBridge;

  @EventListener
  public void handlePredictionEvent(PredictionEvent event) {
    streamBridge.send(configuration.getBindingName(), event);
  }
}
