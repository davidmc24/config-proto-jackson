package config.internal;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.ByteSource;

import java.net.URL;
import java.nio.file.Path;

public class YamlConfigurationSource extends JacksonConfigurationSource {
    public YamlConfigurationSource(Path path) {
        super(path);
    }

    public YamlConfigurationSource(URL url) {
        super(url);
    }

    public YamlConfigurationSource(ByteSource byteSource) {
        super(byteSource);
    }

    @Override
    protected JsonFactory getFactory(ObjectMapper objectMapper) {
        return new YAMLFactory(objectMapper);
    }
}
