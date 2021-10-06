package ai.ameron.sidecar.core.model;

import ai.ameron.sidecar.core.predict.PredictionResponse;
import java.util.Map;

public interface ModelServiceAdapter {
  PredictionResponse predict(Map<String, String> features);
}
