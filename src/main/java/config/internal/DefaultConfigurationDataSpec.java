package config.internal;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import config.ConfigurationData;
import config.ConfigurationDataSpec;
import config.ConfigurationSource;
import ratpack.func.Action;

import java.net.URL;
import java.nio.file.Path;
import java.util.Properties;

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
    public ConfigurationDataSpec json(URL url) {
        add(new JsonConfigurationSource(objectMapper, url));
        return this;
    }

    // TODO: support specifying a prefix?
    @Override
    public ConfigurationDataSpec props(Path path) {
        add(new PropertiesConfigurationSource(objectMapper, path));
        return this;
    }

    @Override
    public ConfigurationDataSpec props(Properties properties) {
        add(new PropertiesConfigurationSource(objectMapper, null, properties));
        return this;
    }

    @Override
    public ConfigurationDataSpec props(URL url) {
        add(new PropertiesConfigurationSource(objectMapper, url));
        return this;
    }

    // TODO: support specifying a prefix?
    @Override
    public ConfigurationDataSpec sysProps() {
        add(new SystemPropertiesConfigurationSource(objectMapper));
        return this;
    }

    @Override
    public ConfigurationDataSpec yaml(Path path) {
        add(new YamlConfigurationSource(objectMapper, path));
        return this;
    }

    @Override
    public ConfigurationDataSpec yaml(URL url) {
        add(new YamlConfigurationSource(objectMapper, url));
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
