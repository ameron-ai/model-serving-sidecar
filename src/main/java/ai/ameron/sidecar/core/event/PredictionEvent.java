package ai.ameron.sidecar.core.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import ai.ameron.sidecar.core.predict.PredictionResponse;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEvent;

public class PredictionEvent extends ApplicationEvent {
  private ObjectMapper objectMapper = new ObjectMapper();
  @Getter
  private PredictionResponse predictionResponse;

  public PredictionEvent(Object source, PredictionResponse predictionResponse) {
    super(source);
    this.predictionResponse = predictionResponse;
  }

  @SneakyThrows
  public String asJson() {
    return objectMapper.writeValueAsString(predictionResponse);
  }
}
