package ai.ameron.sidecar.core.predict;

import static ai.ameron.sidecar.TestUtil.buildNode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.ameron.sidecar.TestUtil;
import ai.ameron.sidecar.core.model.ModelServiceErrorCodes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class PredictionResponseTest {

  Prediction buildSuccessPrediction(){
    return new Prediction(
        false, null, null,
        "success-model", "1.0.0", 100L, buildNode("success"));
  }

  Prediction buildErrorPrediction(){
    return new Prediction(
        true, ModelServiceErrorCodes.ERROR_CALLING_MODEL_SERVICE, "Error",
        "success-model", "1.0.0", 100L, buildNode("success"));
  }

  @Test
  void successPredictionResponse(){
    Set<Prediction> predictions = new HashSet<>();
    predictions.add(buildSuccessPrediction());

    PredictionResponse predictionResponse =
        PredictionResponse.success(100L, buildSuccessPrediction(), predictions);

    assertNotNull(predictionResponse);
    assertEquals(1, predictionResponse.getPredictionCount());
  }

  @Test
  void successPredictionResponseWithNullPredictions(){
    assertThrows(IllegalArgumentException.class, () -> PredictionResponse.success(100L, null, null));
  }

  @Test
  void successPredictionResponseWithEmptyPredictions(){
    assertThrows(IllegalArgumentException.class, () -> PredictionResponse.success(100L, null, new HashSet<>()));
  }

  @Test
  void errorPredictionResponse(){
    PredictionResponse predictionResponse = PredictionResponse.error(
        100L,
        ModelServiceErrorCodes.ERROR_RECEIVED_FROM_MODEL_SERVICE, "An error description",
        buildErrorPrediction(),
        Set.of());

    assertNotNull(predictionResponse);
    assertEquals(ModelServiceErrorCodes.ERROR_RECEIVED_FROM_MODEL_SERVICE, predictionResponse.getErrorCode());
    assertEquals("An error description", predictionResponse.getErrorMessage());
    assertEquals(0, predictionResponse.getPredictionCount());
  }

  @Test
  void errorPredictionResponseWithEmptyErrorCode(){
    assertThrows(IllegalArgumentException.class, () -> PredictionResponse.error(100L, null, "An error description", buildErrorPrediction(), Set.of()));
  }
}
