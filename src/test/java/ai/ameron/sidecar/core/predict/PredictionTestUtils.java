package ai.ameron.sidecar.core.predict;

import static ai.ameron.sidecar.TestUtil.buildNode;

import ai.ameron.sidecar.core.model.ModelServiceErrorCodes;
import java.time.LocalDateTime;

public class PredictionTestUtils {
  static Prediction buildSuccessPrediction(){
    return new Prediction(
        false, null, null,
        "success-model", "1.0.0",
        LocalDateTime.now(), 100L, buildNode("success"));
  }

  static Prediction buildErrorPrediction(){
    ModelServiceErrorCodes error = ModelServiceErrorCodes.ERROR_CALLING_MODEL_SERVICE;
    return new Prediction(
        true, error.getCode(), error.buildErrorMessage(""),
        "success-model", "1.0.0",
        LocalDateTime.now(), 100L, buildNode("failure"));
  }
}
