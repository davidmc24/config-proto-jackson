package config.internal;

import com.fasterxml.jackson.databind.node.ObjectNode;
import config.ConfigurationSource;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesConfigurationSource implements ConfigurationSource {
    public PropertiesConfigurationSource(Properties properties) {

    }

    public PropertiesConfigurationSource(URL url) {

    }

    public PropertiesConfigurationSource(Path path) {

    }

    @Override
    public ObjectNode loadConfigurationData() {
        throw new UnsupportedOperationException("Not yet implemented"); //TODO
    }
}
