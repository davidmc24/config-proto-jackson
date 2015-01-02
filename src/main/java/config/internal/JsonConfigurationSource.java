package config.internal;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteSource;

import java.net.URL;
import java.nio.file.Path;

public class JsonConfigurationSource extends JacksonConfigurationSource {
    public JsonConfigurationSource(Path path) {
        super(path);
    }

    public JsonConfigurationSource(URL url) {
        super(url);
    }

    public JsonConfigurationSource(ByteSource byteSource) {
        super(byteSource);
    }

    @Override
    protected JsonFactory getFactory(ObjectMapper objectMapper) {
        return objectMapper.getFactory();
    }
}
