package config;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ConfigurationSource {
    // TODO... does this return type make sense?  It exposes a jackson type
    ObjectNode loadConfigurationData();
}
