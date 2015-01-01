package config.internal;

import com.fasterxml.jackson.databind.node.ObjectNode;
import config.ConfigurationSource;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class YamlConfigurationSource implements ConfigurationSource {
    public YamlConfigurationSource(URL url) {

    }

    public YamlConfigurationSource(Path path) {

    }

    @Override
    public ObjectNode loadConfigurationData() {
        throw new UnsupportedOperationException("Not yet implemented"); //TODO
    }
}
