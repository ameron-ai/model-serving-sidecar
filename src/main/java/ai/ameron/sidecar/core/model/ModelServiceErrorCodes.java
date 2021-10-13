package ai.ameron.sidecar.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ModelServiceErrorCodes {
  ERROR_RECEIVED_FROM_MODEL_SERVICE("ME001", "Error response received from Model Service: %s"),
  ERROR_CALLING_MODEL_SERVICE("ME002", "Error occurred calling Model: %s");

  @Getter
  private final String code;
  private final String messageTemplate;

  public String buildErrorMessage(Object... args){
    return String.format(messageTemplate, args);
  }
}
