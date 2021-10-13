package ai.ameron.sidecar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TestUtil {
  public static JsonNode buildNode(String text){
    return TextNode.valueOf(text);
  }
}
