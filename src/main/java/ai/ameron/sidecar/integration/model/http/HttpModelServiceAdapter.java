package ai.ameron.sidecar.integration.model.http;

import ai.ameron.sidecar.core.model.ModelServiceErrorCodes;
import ai.ameron.sidecar.core.model.ModelServiceAdapter;
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
      ResponseEntity<ModelPredictionResponse> response, Long timeTakenInMs) {
    Assert.notNull(response, "A successful Model Service call response must have contents");

    ModelPredictionResponse responseBody = response.getBody();
    Assert.notNull(responseBody, "A successful Model Service call response must have contents");

    return PredictionResponse.success(
        timeTakenInMs,
        responseBody.getPrediction() != null ? ModelPrediction.from(responseBody.getPrediction()) : null,
        ModelPrediction.from(responseBody.getSecondaryPredictions()));
  }

  PredictionResponse buildFailurePredictionResponse(
      Long timeTakenInMs,
      String errorMessage,
      ResponseEntity<ModelPredictionResponse> response){
    if(response != null && response.getBody() != null){
      ModelPredictionResponse modelPredictionResponse = response.getBody();

      return PredictionResponse.error(
          timeTakenInMs, ModelServiceErrorCodes.ERROR_RECEIVED_FROM_MODEL_SERVICE, errorMessage,
          ModelPrediction.from(modelPredictionResponse.getPrediction()),
          ModelPrediction.from(modelPredictionResponse.getSecondaryPredictions()));
    } else {
      return PredictionResponse.error(
          timeTakenInMs, ModelServiceErrorCodes.ERROR_RECEIVED_FROM_MODEL_SERVICE, errorMessage);
    }
  }

  String buildFailureResponseErrorMessage(ResponseEntity<ModelPredictionResponse> response){
    return "Error response received from Model Service: " +
        (response != null ? response.getStatusCodeValue() : "");
  }

  PredictionResponse handleFailureResponse(
      ResponseEntity<ModelPredictionResponse> response, Long timeTakenInMs) {
    String errorMessage = buildFailureResponseErrorMessage(response);
    log.error(errorMessage);

    return buildFailurePredictionResponse(timeTakenInMs, errorMessage, response);
  }

  PredictionResponse handleRestClientException(Long timeTakenInMs, RestClientException rce) {
    String errorMessage = "Error occurred calling Model: " + rce.getMessage();
    log.error(errorMessage, rce);

    return PredictionResponse.error(
        timeTakenInMs, ModelServiceErrorCodes.ERROR_CALLING_MODEL_SERVICE, errorMessage);
  }

  @Override
  public PredictionResponse predict(Map<String, String> features){
    PredictionResponse predictionResponse;

    Long start = System.currentTimeMillis();
    try {
      ResponseEntity<ModelPredictionResponse> response = callModelService(features);
      Long timeTakenInMs = System.currentTimeMillis() - start;

      if(isSuccessResponse(response)){
        predictionResponse = handleSuccessResponse(response, timeTakenInMs);
      }  else {
        predictionResponse = handleFailureResponse(response, timeTakenInMs);
      }
    } catch (RestClientException rce){
      Long timeTakenInMs = System.currentTimeMillis() - start;
      predictionResponse = handleRestClientException(timeTakenInMs, rce);
    }

    return predictionResponse;
  }
}