package config.internal;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.nio.file.Path;

public class JsonConfigurationSource extends JacksonConfigurationSource {
    public JsonConfigurationSource(ObjectMapper objectMapper, Path path) {
        super(objectMapper, path);
    }

    public JsonConfigurationSource(ObjectMapper objectMapper, URL url) {
        super(objectMapper, url);
    }

    @Override
    protected JsonFactory getFactory() {
        return getObjectMapper().getFactory();
    }
}
