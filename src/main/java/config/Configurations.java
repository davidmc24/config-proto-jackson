package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.internal.DefaultConfigurationDataSpec;

public class Configurations {
    public static ConfigurationDataSpec config() {
        return new DefaultConfigurationDataSpec();
    }

    public static ConfigurationDataSpec config(ObjectMapper objectMapper) {
        return new DefaultConfigurationDataSpec(objectMapper);
    }
}
