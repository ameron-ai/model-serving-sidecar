package ai.ameron.sidecar.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.MissingFormatArgumentException;
import org.junit.jupiter.api.Test;

class ModelServiceErrorCodesTest {

  @Test
  void buildErrorMessage() {
    String message = ModelServiceErrorCodes.ERROR_RECEIVED_FROM_MODEL_SERVICE.buildErrorMessage(500L);
    assertNotNull(message);
    assertEquals("Error response received from Model Service: 500", message);
  }

  @Test
  void buildErrorMessageWithMissingArguments() {
    assertThrows(
        MissingFormatArgumentException.class,
        () -> ModelServiceErrorCodes.ERROR_RECEIVED_FROM_MODEL_SERVICE.buildErrorMessage());
  }
}