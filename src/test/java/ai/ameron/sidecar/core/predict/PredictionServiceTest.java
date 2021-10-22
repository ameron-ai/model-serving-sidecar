package ai.ameron.sidecar.core.predict;

import static ai.ameron.sidecar.core.predict.PredictionTestUtils.buildSuccessPrediction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.ameron.sidecar.core.model.ModelServiceAdapter;
import java.util.HashMap;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class PredictionServiceTest {
  private PredictionService predictionService;
  @Mock
  private ModelServiceAdapter modelServiceAdapter;
  @Mock
  private ApplicationEventPublisher publisher;

  @BeforeEach
  void beforeEach(){
    predictionService = new PredictionService(modelServiceAdapter, publisher);
  }

  @Test
  void predict() {
    Prediction testPrimaryPrediction = buildSuccessPrediction();
    Prediction testPrediction = buildSuccessPrediction();
    PredictionResponse predictionResponse = PredictionResponse.success(0L, testPrimaryPrediction, Set.of(testPrediction));
    when(modelServiceAdapter.predict(any())).thenReturn(predictionResponse);

    PredictionResponse response = predictionService.predict(new HashMap<>());

    verify(publisher, times(1)).publishEvent(any(ApplicationEvent.class));
    assertNotNull(response);
    assertEquals(1, response.getPredictionCount());
    assertEquals(testPrimaryPrediction.getModelName(), response.getPrediction().getModelName());
  }
}