package ai.ameron.sidecar.core.predict;

import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PredictionRequest {
  @NotNull(message = "A Map of Features called 'features' is required")
  private Map<String, String> features;
}
