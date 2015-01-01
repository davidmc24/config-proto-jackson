package config.internal;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class SystemPropertiesConfigurationSource extends PropertiesConfigurationSource {
    public static final String DEFAULT_PREFIX = "ratpack.";

    public SystemPropertiesConfigurationSource(ObjectMapper objectMapper) {
        this(objectMapper, DEFAULT_PREFIX);
    }

    public SystemPropertiesConfigurationSource(ObjectMapper objectMapper, String prefix) {
        super(objectMapper, prefix, System.getProperties());
    }
}
