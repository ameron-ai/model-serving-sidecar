package ai.ameron.sidecar.core.predict;

import ai.ameron.sidecar.core.event.PredictionEvent;
import ai.ameron.sidecar.core.model.ModelServiceAdapter;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class PredictionService {
  private ModelServiceAdapter modelServiceAdapter;
  private ApplicationEventPublisher applicationEventPublisher;

  public PredictionResponse predict(Map<String, String> features){
    PredictionResponse predictionResponse = modelServiceAdapter.predict(features);

    applicationEventPublisher.publishEvent(new PredictionEvent(this, predictionResponse));
    return predictionResponse;
  }
}
