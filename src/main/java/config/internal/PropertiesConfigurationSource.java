package config.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import config.ConfigurationSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class PropertiesConfigurationSource implements ConfigurationSource {
    private final ObjectMapper objectMapper;
    private final ObjectNode node;

    public PropertiesConfigurationSource(ObjectMapper objectMapper, Properties properties) {
        this.objectMapper = objectMapper;
        this.node = objectMapper.createObjectNode();
        Splitter splitter = Splitter.on(".");
        for (String key : properties.stringPropertyNames()) {
            List<String> keyParts = splitter.splitToList(key);
            String value = properties.getProperty(key);
            putValue(keyParts, value, node);
        }
        // TODO: pull in richer impl from https://github.com/danveloper/config-binding/blob/master/src/main/java/config/PropertiesConfigurationSource.java
    }

    private void putValue(Iterable<String> keyParts, String value, ObjectNode node) {
        String curPart = Iterables.getFirst(keyParts, null);
        if (Iterables.size(keyParts) == 1) {
            node.set(curPart, TextNode.valueOf(value));
        } else {
            ObjectNode childNode = (ObjectNode) node.get(curPart);
            if (childNode == null) {
                childNode = objectMapper.createObjectNode();
                node.set(curPart, childNode);
            }
            putValue(Iterables.skip(keyParts, 1), value, childNode);
        }
    }

    public PropertiesConfigurationSource(ObjectMapper objectMapper, URL url) {
        this(objectMapper, load(Resources.asByteSource(url)));
    }

    public PropertiesConfigurationSource(ObjectMapper objectMapper, Path path) {
        this(objectMapper, load(Files.asByteSource(path.toFile())));
    }

    @Override
    public ObjectNode loadConfigurationData() {
        return node;
    }

    private static Properties load(ByteSource byteSource) {
        Properties properties = new Properties();
        try (InputStream inputStream = byteSource.openStream()) {
            properties.load(inputStream);
        } catch (IOException ex) {
            throw Throwables.propagate(ex);
        }
        return properties;
    }
}
