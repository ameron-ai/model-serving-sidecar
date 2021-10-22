package ai.ameron.sidecar.core.predict;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class PredictionResponse {
  private Long timeTakenInMs;

  private boolean error = false;
  private String errorCode = null;
  private String errorMessage = null;

  private Prediction prediction;
  private Set<Prediction> secondaryPredictions = new HashSet<>();

  private PredictionResponse(
      Long timeTakenInMs, Prediction prediction, Set<Prediction> secondaryPredictions) {
    this.timeTakenInMs = timeTakenInMs;
    this.prediction = prediction;

    if(secondaryPredictions != null)
      this.secondaryPredictions.addAll(secondaryPredictions);
  }

  private PredictionResponse(
      Long timeTakenInMs, String errorCode, String errorMessage,
      Prediction prediction, Set<Prediction> secondaryPredictions) {
    this.timeTakenInMs = timeTakenInMs;
    this.error = true;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.prediction = prediction;

    if(secondaryPredictions != null)
      this.secondaryPredictions.addAll(secondaryPredictions);
  }

  private static boolean hasAtLeastOnePrediction(Prediction prediction, Set<Prediction> secondaryPredictions){
    return prediction != null ||
        (secondaryPredictions != null && !secondaryPredictions.isEmpty());
  }

  public static PredictionResponse success(
      Long timeTakenInMs, Prediction prediction, Set<Prediction> secondaryPredictions) {
    Assert.isTrue(hasAtLeastOnePrediction(prediction, secondaryPredictions),
        "At least one Prediction is required");

    return new PredictionResponse(timeTakenInMs, prediction, secondaryPredictions);
  }

  public static PredictionResponse error(
      Long timeTakenInMs, String errorCode, String errorMessage,
      Prediction prediction, Set<Prediction> secondaryPredictions) {
    Assert.hasLength(errorCode, "errorCode cannot be null or empty");
    return new PredictionResponse(timeTakenInMs, errorCode, errorMessage, prediction, secondaryPredictions);
  }

  public static PredictionResponse error(
      Long timeTakenInMs, String errorCode, String errorMessage) {
    return error(timeTakenInMs, errorCode, errorMessage, null, Set.of());
  }

  public int getPredictionCount() {
    return secondaryPredictions.size() + (prediction != null ? 1 : 0);
  }
}

