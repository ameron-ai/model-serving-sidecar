package ai.ameron.sidecar.integration.model.http;

import ai.ameron.sidecar.core.predict.Prediction;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModelPredictionResponse {
  private Prediction prediction;
  private Set<Prediction> secondaryPredictions = new HashSet<>();
}

