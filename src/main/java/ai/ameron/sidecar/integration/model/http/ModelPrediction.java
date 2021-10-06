package ai.ameron.sidecar.integration.model.http;

import ai.ameron.sidecar.core.predict.Prediction;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModelPrediction {
  private boolean error = false;
  private String errorCode = null;
  private String errorMessage = null;

  private String modelName;
  private String modelVersion;
  private Long timeTakenInMs;

  private String value;

  public static Prediction from(ModelPrediction modelPrediction) {
    Assert.notNull(modelPrediction, "modelPrediction cannot be null");

    return new Prediction(
        modelPrediction.isError(), modelPrediction.getErrorCode(), modelPrediction.getErrorMessage(),
        modelPrediction.getModelName(), modelPrediction.getModelVersion(),
        modelPrediction.getTimeTakenInMs(), modelPrediction.getValue());
  }

  public static Set<Prediction> from(Set<ModelPrediction> modelPredictions){
    if(modelPredictions != null){
      return modelPredictions
          .stream()
          .map(ModelPrediction::from)
          .collect(Collectors.toSet());
    } else {
      return Set.of();
    }
  }
}
