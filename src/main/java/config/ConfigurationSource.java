package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ConfigurationSource {
    // TODO... does this signature make sense?  It exposes a jackson types
    ObjectNode loadConfigurationData(ObjectMapper objectMapper);
}
