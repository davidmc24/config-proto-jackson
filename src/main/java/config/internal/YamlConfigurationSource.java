package config.internal;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.net.URL;
import java.nio.file.Path;

public class YamlConfigurationSource extends JacksonConfigurationSource {
    public YamlConfigurationSource(ObjectMapper objectMapper, Path path) {
        super(objectMapper, path);
    }

    public YamlConfigurationSource(ObjectMapper objectMapper, URL url) {
        super(objectMapper, url);
    }

    @Override
    protected JsonFactory getFactory() {
        return new YAMLFactory(getObjectMapper());
    }
}
