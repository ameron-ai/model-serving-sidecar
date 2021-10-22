package ai.ameron.sidecar.integration.reporter.log;

import ai.ameron.sidecar.core.event.PredictionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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
@AllArgsConstructor
public class LoggerPredictionEventConsumer {
  private ObjectMapper objectMapper;

  @SneakyThrows
  @EventListener
  public void handlePredictionEvent(PredictionEvent event) {
    log.debug(objectMapper.writeValueAsString(event));
  }
}
