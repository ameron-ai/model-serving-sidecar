package ai.ameron.sidecar.integration.provider.rest;

import ai.ameron.sidecar.core.predict.PredictionRequest;
import ai.ameron.sidecar.core.predict.PredictionResponse;
import ai.ameron.sidecar.core.predict.PredictionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping( path = "/api")
public class PredictionRequestController {
  private PredictionService service;

  @PostMapping(path="/predict",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public PredictionResponse predict(@RequestBody PredictionRequest request){
    return service.predict(request.getFeatures());
  }
}