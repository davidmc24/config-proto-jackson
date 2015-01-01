package config.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Throwables;
import config.ConfigurationSource;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class JsonConfigurationSource implements ConfigurationSource {
    private final ObjectMapper objectMapper;
    private final JsonParser parser;

    public JsonConfigurationSource(ObjectMapper objectMapper, URL url) {
        this.objectMapper = objectMapper;
        try {
            parser = objectMapper.getFactory().createParser(url);
        } catch (IOException ex) {
            throw Throwables.propagate(ex);
        }
    }

    public JsonConfigurationSource(ObjectMapper objectMapper, Path path) {
        this.objectMapper = objectMapper;
        try {
            parser = objectMapper.getFactory().createParser(path.toFile());
        } catch (IOException ex) {
            throw Throwables.propagate(ex);
        }
    }

    @Override
    public ObjectNode loadConfigurationData() {
        try {
            return objectMapper.readTree(parser);
        } catch (IOException ex) {
            throw Throwables.propagate(ex);
        }
    }
}
