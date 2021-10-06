package ai.ameron.sidecar.integration.model.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.ameron.sidecar.core.predict.Prediction;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ModelPredictionTest {

  ModelPrediction buildSuccessModelPrediction(){
    return new ModelPrediction(false, null, null, "test-model", "1.0.0", 100L, "A");
  }

  private void assertMatch(ModelPrediction modelPrediction, Prediction prediction) {
    assertEquals(modelPrediction.isError(), prediction.isError());
    assertEquals(modelPrediction.getErrorCode(), prediction.getErrorCode());
    assertEquals(modelPrediction.getErrorMessage(), prediction.getErrorMessage());
    assertEquals(modelPrediction.getModelName(), prediction.getModelName());
    assertEquals(modelPrediction.getModelVersion(), prediction.getModelVersion());
    assertEquals(modelPrediction.getTimeTakenInMs(), prediction.getTimeTakenInMs());
    assertEquals(modelPrediction.getValue(), prediction.getValue());
  }

  @Test
  void fromNonEmptyModelPredictions() {
    ModelPrediction modelPrediction = buildSuccessModelPrediction();
    Set<Prediction> predictions = ModelPrediction.from(Set.of(modelPrediction));

    assertNotNull(predictions);
    assertEquals(1, predictions.size());

    Prediction prediction = predictions.iterator().next();
    assertMatch(modelPrediction, prediction);
  }

  @Test
  void fromWithNullModelPredictions() {
    Set<Prediction> predictions = ModelPrediction.from((Set)null);

    assertNotNull(predictions);
    assertEquals(0, predictions.size());
  }

  @Test
  void fromWithEmptyPredictions() {
    Set<Prediction> predictions = ModelPrediction.from(Set.of());

    assertNotNull(predictions);
    assertEquals(0, predictions.size());
  }

  @Test
  void fromWitPrediction(){
    assertThrows(IllegalArgumentException.class, () -> ModelPrediction.from((ModelPrediction)null));
  }

  @Test
  void fromWithNullPrediction(){
    ModelPrediction modelPrediction = buildSuccessModelPrediction();
    Prediction prediction = ModelPrediction.from(modelPrediction);

    assertNotNull(prediction);
    assertMatch(modelPrediction, prediction);
  }
}