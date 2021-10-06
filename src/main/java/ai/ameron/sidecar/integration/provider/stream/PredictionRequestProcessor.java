package ai.ameron.sidecar.integration.provider.stream;

import ai.ameron.sidecar.core.predict.PredictionRequest;
import ai.ameron.sidecar.core.predict.PredictionResponse;
import ai.ameron.sidecar.core.predict.PredictionService;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component("predictionRequestProcessor")
public class PredictionRequestProcessor implements Function<PredictionRequest, PredictionResponse> {
  private PredictionService service;

  @Override
  public PredictionResponse apply(PredictionRequest predictionRequest) {
    return service.predict(predictionRequest.getFeatures());
  }
}
