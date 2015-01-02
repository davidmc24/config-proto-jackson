package config.internal;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import config.ConfigurationSource;
import ratpack.util.ExceptionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

public abstract class JacksonConfigurationSource implements ConfigurationSource {
    private final ByteSource byteSource;

    public JacksonConfigurationSource(Path path) {
        this(Files.asByteSource(path.toFile()));
    }

    public JacksonConfigurationSource(URL url) {
        this(Resources.asByteSource(url));
    }

    public JacksonConfigurationSource(ByteSource byteSource) {
        this.byteSource = byteSource;
    }

    @Override
    public ObjectNode loadConfigurationData(ObjectMapper objectMapper) {
        try (InputStream inputStream = byteSource.openStream()) {
            JsonParser parser = getFactory(objectMapper).createParser(inputStream);
            return objectMapper.readTree(parser);
        } catch (IOException ex) {
            throw ExceptionUtils.uncheck(ex);
        }
    }

    protected abstract JsonFactory getFactory(ObjectMapper objectMapper);
}
