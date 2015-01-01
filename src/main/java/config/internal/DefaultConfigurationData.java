package config.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import config.ConfigurationData;
import config.ConfigurationSource;
import ratpack.launch.ServerConfig;

import java.io.IOException;
import java.util.Iterator;

public class DefaultConfigurationData implements ConfigurationData {
    // TODO: cleanup
    private static final ImmutableMap CLASS_SUBSTITUTIONS = ImmutableMap.of(); //  ImmutableMap.of(ServerConfig.class, AnnotatedDefaultServerConfig.class);
    private final ObjectMapper objectMapper;
    private final ObjectNode rootNode;

    public DefaultConfigurationData(ObjectMapper objectMapper, ImmutableList<ConfigurationSource> configurationSources) {
        this.objectMapper = objectMapper;
        rootNode = objectMapper.createObjectNode();
        for (ConfigurationSource source : configurationSources) {
            merge(source.loadConfigurationData(), rootNode);
        }
    }

    @Override
    public <O> O get(Class<O> type) {
        Class<? extends O> actualType = determineType(Preconditions.checkNotNull(type));
        try {
            return objectMapper.readValue(new TreeTraversingParser(rootNode, objectMapper), actualType);
        } catch (IOException ex) {
            throw Throwables.propagate(ex);
        }
    }

    /**
     * Merges node data from the source into the dest, overwriting non-object fields in dest if they already exist.
     */
    private void merge(JsonNode sourceNode, JsonNode destNode) {
        Iterator<String> fieldNames = sourceNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode sourceFieldValue = sourceNode.get(fieldName);
            JsonNode destFieldValue = destNode.get(fieldName);
            if (destFieldValue != null && destFieldValue.isObject()) {
                merge(sourceFieldValue, destFieldValue);
            } else if (destNode instanceof ObjectNode) {
                ((ObjectNode) destNode).replace(fieldName, sourceFieldValue);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <O> Class<? extends O> determineType(Class<O> type) {
        return (Class<? extends O>) CLASS_SUBSTITUTIONS.getOrDefault(type, type);
    }
}
