package config.internal;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import config.ConfigurationData;
import config.ConfigurationDataSpec;
import config.ConfigurationSource;
import ratpack.func.Action;

import java.nio.file.Path;

public class DefaultConfigurationDataSpec implements ConfigurationDataSpec {
    private final ObjectMapper objectMapper;
    private final ImmutableList.Builder<ConfigurationSource> sources = ImmutableList.builder();

    public DefaultConfigurationDataSpec() {
        this(newDefaultObjectMapper());
    }

    public DefaultConfigurationDataSpec(ObjectMapper objectMapper) {
        this.objectMapper = Preconditions.checkNotNull(objectMapper);
    }

    @Override
    public ConfigurationDataSpec add(ConfigurationSource configurationSource) {
        Preconditions.checkNotNull(configurationSource);
        sources.add(configurationSource);
        return this;
    }

    @Override
    public ConfigurationData build() {
        return new DefaultConfigurationData(objectMapper, sources.build());
    }

    @Override
    public ConfigurationDataSpec configureObjectMapper(Action<ObjectMapper> action) {
        try {
            action.execute(objectMapper);
        } catch (Exception ex) {
            throw Throwables.propagate(ex);
        }
        return this;
    }

    @Override
    public ConfigurationDataSpec json(Path path) {
        add(new JsonConfigurationSource(objectMapper, path));
        return this;
    }

    @Override
    public ConfigurationDataSpec yaml(Path path) {
        add(new YamlConfigurationSource(objectMapper, path));
        return this;
    }

    private static ObjectMapper newDefaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        objectMapper.registerModule(new ConfigurationModule());
        //objectMapper.setSubtypeResolver(new DiscoverableSubtypeResolver());
        JsonFactory factory = objectMapper.getFactory();
        factory.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        return objectMapper;
    }
}
