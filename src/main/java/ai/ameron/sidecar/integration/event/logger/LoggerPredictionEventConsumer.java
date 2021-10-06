package ai.ameron.sidecar.integration.event.logger;

import ai.ameron.sidecar.core.event.PredictionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * LoggerPredictionEventConsumer provides a no functionality implementation for testing and
 * troubleshooting upstream components.
 */
@Slf4j
@ConditionalOnProperty(
    value="app.prediction-event.consumer",
    havingValue = "logger")
@Service
public class LoggerPredictionEventConsumer {
  @EventListener
  public void handlePredictionEvent(PredictionEvent event) {
    log.debug(event.asJson());
  }
}
