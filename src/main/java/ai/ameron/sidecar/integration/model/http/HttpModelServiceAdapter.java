package ai.ameron.sidecar.integration.model.http;

import static ai.ameron.sidecar.core.model.ModelServiceErrorCodes.ERROR_RECEIVED_FROM_MODEL_SERVICE;

import ai.ameron.sidecar.core.model.ModelServiceAdapter;
import ai.ameron.sidecar.core.model.ModelServiceErrorCodes;
import ai.ameron.sidecar.core.predict.PredictionResponse;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(
    value="app.model-service.adapter.backend",
    havingValue = "http")
@Service
public class HttpModelServiceAdapter implements ModelServiceAdapter {
  private RestTemplate restTemplate;
  private HttpPredictionServiceConfiguration configuration;

  boolean isSuccessResponse(ResponseEntity<ModelPredictionResponse> response){
    return response != null &&
        HttpStatus.OK.value() == response.getStatusCodeValue();
  }

  ResponseEntity<ModelPredictionResponse> callModelService(Map<String, String> features) {
    return restTemplate.postForEntity(configuration.getModelServiceUrl(),
            new ModelPredictionRequest(features),
            ModelPredictionResponse.class);
  }

  PredictionResponse handleSuccessResponse(
      ResponseEntity<ModelPredictionResponse> response, long timeTakenInMs) {
    Assert.notNull(response, "A successful Model Service call response must have contents");

    ModelPredictionResponse responseBody = response.getBody();
    Assert.notNull(responseBody, "A successful Model Service call response must have contents");

    return PredictionResponse.success(
        timeTakenInMs,
        responseBody.getPrediction(),
        responseBody.getSecondaryPredictions());
  }

  PredictionResponse buildFailurePredictionResponse(
      long timeTakenInMs,
      ResponseEntity<ModelPredictionResponse> response){
    ModelServiceErrorCodes error = ERROR_RECEIVED_FROM_MODEL_SERVICE;

    if(response != null && response.getBody() != null){
      ModelPredictionResponse modelPredictionResponse = response.getBody();
      String errorMessage = error.buildErrorMessage(response.getStatusCodeValue());

      return PredictionResponse.error(
          timeTakenInMs, error.getCode(), errorMessage,
          modelPredictionResponse.getPrediction(),
          modelPredictionResponse.getSecondaryPredictions());
    } else {
      String errorMessage = error.buildErrorMessage("");
      return PredictionResponse.error(timeTakenInMs, error.getCode(), errorMessage);
    }
  }

  PredictionResponse handleFailureResponse(
      ResponseEntity<ModelPredictionResponse> response, Long timeTakenInMs) {
    PredictionResponse predictionResponse = buildFailurePredictionResponse(timeTakenInMs, response);
    log.error(predictionResponse.getErrorMessage());
    return predictionResponse;
  }

  PredictionResponse handleRestClientException(Long timeTakenInMs, RestClientException rce) {
    ModelServiceErrorCodes error = ModelServiceErrorCodes.ERROR_CALLING_MODEL_SERVICE;
    String errorMessage = error.buildErrorMessage(rce.getMessage());
    log.error(errorMessage, rce);

    return PredictionResponse.error(timeTakenInMs, error.getCode(), errorMessage);
  }

  @Override
  public PredictionResponse predict(Map<String, String> features){
    PredictionResponse predictionResponse;

    long start = System.currentTimeMillis();
    try {
      ResponseEntity<ModelPredictionResponse> response = callModelService(features);
      long timeTakenInMs = System.currentTimeMillis() - start;

      if(isSuccessResponse(response)){
        predictionResponse = handleSuccessResponse(response, timeTakenInMs);
      }  else {
        predictionResponse = handleFailureResponse(response, timeTakenInMs);
      }
    } catch (RestClientException rce){
      long timeTakenInMs = System.currentTimeMillis() - start;
      predictionResponse = handleRestClientException(timeTakenInMs, rce);
    }

    return predictionResponse;
  }
}