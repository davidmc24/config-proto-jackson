package config.internal;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class SystemPropertiesConfigurationSource extends PropertiesConfigurationSource {
    public SystemPropertiesConfigurationSource(ObjectMapper objectMapper) {
        super(objectMapper, System.getProperties());
    }
}
