package config.internal;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Throwables;
import config.ConfigurationSource;
import ratpack.util.ExceptionUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public abstract class JacksonConfigurationSource implements ConfigurationSource {
    private final ObjectMapper objectMapper;
    private final JsonParser parser;

    public JacksonConfigurationSource(ObjectMapper objectMapper, Path path) {
        this.objectMapper = objectMapper;
        try {
            parser = getFactory().createParser(path.toFile());
        } catch (IOException ex) {
            throw ExceptionUtils.uncheck(ex);
        }
    }

    public JacksonConfigurationSource(ObjectMapper objectMapper, URL url) {
        this.objectMapper = objectMapper;
        try {
            parser = getFactory().createParser(url);
        } catch (IOException ex) {
            throw ExceptionUtils.uncheck(ex);
        }
    }

    @Override
    public ObjectNode loadConfigurationData() {
        try {
            return objectMapper.readTree(parser);
        } catch (IOException ex) {
            throw ExceptionUtils.uncheck(ex);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected abstract JsonFactory getFactory();
}
