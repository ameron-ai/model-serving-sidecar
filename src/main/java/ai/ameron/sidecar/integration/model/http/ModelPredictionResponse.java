package ai.ameron.sidecar.integration.model.http;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModelPredictionResponse {
  private ModelPrediction prediction;
  private Set<ModelPrediction> secondaryPredictions = new HashSet<>();
}

