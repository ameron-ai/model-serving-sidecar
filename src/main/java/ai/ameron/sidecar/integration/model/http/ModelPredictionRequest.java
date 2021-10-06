package ai.ameron.sidecar.integration.model.http;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModelPredictionRequest {
  private Map<String, String> features;
}
