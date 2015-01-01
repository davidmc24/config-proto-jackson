package config.internal;

import com.fasterxml.jackson.databind.node.ObjectNode;
import config.ConfigurationSource;

public class EnvironmentVariablesConfigurationSource implements ConfigurationSource {
    @Override
    public ObjectNode loadConfigurationData() {
        throw new UnsupportedOperationException("Not yet implemented"); //TODO
    }
}
