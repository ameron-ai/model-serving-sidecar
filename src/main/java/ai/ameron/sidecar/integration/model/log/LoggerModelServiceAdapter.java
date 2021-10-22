package ai.ameron.sidecar.integration.model.log;

import ai.ameron.sidecar.core.model.ModelServiceAdapter;
import ai.ameron.sidecar.core.predict.Prediction;
import ai.ameron.sidecar.core.predict.PredictionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * LoggerModelService provides a no functionality implementation for testing and troubleshooting
 * upstream components.
 */
@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(
    value="app.model-service.adapter.backend",
    havingValue = "logger")
@Service
public class LoggerModelServiceAdapter implements ModelServiceAdapter {
  private ObjectMapper objectMapper;

  @SneakyThrows
  @Override
  public PredictionResponse predict(Map<String, String> features) {
    log.info("Logging predict request for:" + objectMapper.writeValueAsString(features));

    Prediction prediction = new Prediction(
        false, null, null,
        "logger-model", "1.0.0", LocalDateTime.now(), 0L,
        TextNode.valueOf(""));
    return PredictionResponse.success(0L, prediction, Set.of());
  }
}
