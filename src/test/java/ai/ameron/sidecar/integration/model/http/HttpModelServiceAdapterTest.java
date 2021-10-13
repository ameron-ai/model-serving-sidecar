package ai.ameron.sidecar.integration.model.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import ai.ameron.sidecar.TestUtil;
import ai.ameron.sidecar.core.model.ModelServiceErrorCodes;
import ai.ameron.sidecar.core.predict.PredictionResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class HttpModelServiceAdapterTest {
  @Mock
  private RestTemplate restTemplate;
  private HttpPredictionServiceConfiguration configuration;
  private HttpModelServiceAdapter service;

  @BeforeEach
  void beforeEach(){
    configuration = new HttpPredictionServiceConfiguration();
    configuration.setModelServiceUrl("http://localhost");

    service = new HttpModelServiceAdapter(restTemplate, configuration);
  }

  ModelPredictionResponse emptyResponse(){
    return new ModelPredictionResponse();
  }

  @Test
  void isSuccessResponse(){
    assertTrue(service.isSuccessResponse(ResponseEntity.ok(emptyResponse())));
  }

  @Test
  void isNotSuccessResponse(){
    assertFalse(service.isSuccessResponse(ResponseEntity.status(500).body(emptyResponse())));
  }

  @Test
  void handleRestClientException(){
    PredictionResponse response = service.handleRestClientException(
        100L, new RestClientException("Test Exception"));

    assertNotNull(response);
    assertNull(response.getPrediction());
    assertNotNull(response.getSecondaryPredictions());
    assertEquals(100, response.getTimeTakenInMs());
  }

  @Test
  void buildFailureResponseErrorMessage(){
    ModelPredictionResponse body = new ModelPredictionResponse(new ModelPrediction(), Set.of());
    String result = service.buildFailureResponseErrorMessage(ResponseEntity.status(200).body(body));

    assertNotNull(result);
    assertTrue(result.endsWith("200"));
  }

  @Test
  void buildFailureResponseErrorMessageNullResponse(){
    String result = service.buildFailureResponseErrorMessage(null);

    assertNotNull(result);
  }

  ModelPrediction buildSuccessModelPrediction(){
    return new ModelPrediction(false, null, null, "test-model", "1.0.0", 100L, TestUtil.buildNode("success"));
  }

  ModelPrediction buildErrorModelPrediction(){
    return new ModelPrediction(true, ModelServiceErrorCodes.ERROR_CALLING_MODEL_SERVICE, null, "test-model", "1.0.0", 100L, null);
  }

  @Test
  void handleResponseSuccessWithNoPredictions(){
    ModelPredictionResponse body = new ModelPredictionResponse(null, Set.of());
    ResponseEntity<ModelPredictionResponse> modelResponse = new ResponseEntity<>(body, HttpStatus.OK);

    assertThrows(IllegalArgumentException.class, () -> service.handleSuccessResponse(modelResponse, 100L));
  }

  @Test
  void handleResponseFailureWithNullResponse(){
    PredictionResponse predictionResponse = service.handleFailureResponse(null, 100L);

    assertNotNull(predictionResponse);
    assertEquals(0, predictionResponse.getPredictionCount());
  }

  @Test
  void handleResponseFailureWithNoPredictions(){
    ResponseEntity<ModelPredictionResponse> response =
        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    PredictionResponse predictionResponse = service.handleFailureResponse(response, 100L);

    assertNotNull(predictionResponse);
    assertEquals(0, predictionResponse.getPredictionCount());
  }

  private Map<String, String> buildTestFeatures() {
    Map<String, String> features = new HashMap<>();
    features.put("feature_1", "C1");
    features.put("feature_2", "200.00");

    return features;
  }

  private ResponseEntity<ModelPredictionResponse> buildValidPredictionResponse(){
    ModelPredictionResponse response =
        new ModelPredictionResponse(buildSuccessModelPrediction(), Set.of(buildSuccessModelPrediction()));
    return ResponseEntity.ok(response);
  }

  private ResponseEntity<ModelPredictionResponse> buildErrorPredictionResponse(){
    return ResponseEntity.status(500).body(null);
  }

  @Test
  void predict() {
    when(restTemplate.postForEntity(
        eq(configuration.getModelServiceUrl()),
        any(ModelPredictionRequest.class),
        eq(ModelPredictionResponse.class)))
        .thenReturn(buildValidPredictionResponse());

    PredictionResponse predictionResponse = service.predict(buildTestFeatures());

    assertNotNull(predictionResponse);
    assertFalse(predictionResponse.isError());
    assertNull(predictionResponse.getErrorCode());
    assertNull(predictionResponse.getErrorMessage());

    assertEquals(1, predictionResponse.getPredictionCount());
    assertTrue(predictionResponse.getSecondaryPredictions().iterator().hasNext());
  }

  @Test
  void predictError() {
    when(restTemplate.postForEntity(
        eq(configuration.getModelServiceUrl()),
        any(ModelPredictionRequest.class),
        eq(ModelPredictionResponse.class)))
        .thenReturn(buildErrorPredictionResponse());

    PredictionResponse predictionResponse = service.predict(buildTestFeatures());

    assertNotNull(predictionResponse);
    assertTrue(predictionResponse.isError());
    assertNotNull(predictionResponse.getErrorMessage());

    assertEquals(0, predictionResponse.getPredictionCount());
    assertFalse(predictionResponse.getSecondaryPredictions().iterator().hasNext());
  }

  @Test
  void predictRESTError() {
    when(restTemplate.postForEntity(
        eq(configuration.getModelServiceUrl()),
        any(ModelPredictionRequest.class),
        eq(ModelPredictionResponse.class)))
        .thenThrow(new RestClientException("Test Exception"));

    PredictionResponse predictionResponse = service.predict(buildTestFeatures());

    assertNotNull(predictionResponse);
    assertTrue(predictionResponse.isError());
    assertNotNull(predictionResponse.getErrorMessage());

    assertEquals(0, predictionResponse.getPredictionCount());
    assertFalse(predictionResponse.getSecondaryPredictions().iterator().hasNext());
  }
}