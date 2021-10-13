package ai.ameron.sidecar.core.predict;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Prediction {
  private boolean error = false;
  private String errorCode = null;
  private String errorMessage = null;

  private String modelName;
  private String modelVersion;

  private Long timeTakenInMs;
  private JsonNode value;
}
