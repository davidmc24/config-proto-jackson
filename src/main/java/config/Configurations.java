package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.internal.*;

import java.net.URL;
import java.nio.file.Path;
import java.util.Properties;

public class Configurations {
    public static ConfigurationDataSpec config() {
        return new DefaultConfigurationDataSpec();
    }

    public static ConfigurationDataSpec config(ObjectMapper objectMapper) {
        // TODO: does this make sense?  If any of our customizations are mandatory and internal, this won't be useable
        return new DefaultConfigurationDataSpec(objectMapper);
    }


    // TODO: cleanup
//    public static ConfigurationDataSpec add(ConfigurationSource configurationSource) {
//        return newSpec().add(configurationSource);
//    }
//
//    public static ConfigurationDataSpec env() {
//        return newSpec().env();
//    }
//
//    public static ConfigurationDataSpec json(Path path) {
//        return newSpec().json(path);
//    }
//
//    public static ConfigurationDataSpec json(URL url) {
//        return newSpec().json(url);
//    }
//
//    public static ConfigurationDataSpec props(Path path) {
//        return newSpec().props(path);
//    }
//
//    public static ConfigurationDataSpec props(Properties properties) {
//        return newSpec().props(properties);
//    }
//
//    public static ConfigurationDataSpec props(URL url) {
//        return newSpec().props(url);
//    }
//
//    public static ConfigurationDataSpec sysProps() {
//        return newSpec().sysProps();
//    }
//
//    public static ConfigurationDataSpec yaml(Path path) {
//        return newSpec().yaml(path);
//    }
//
//    public static ConfigurationDataSpec yaml(URL url) {
//        return newSpec().yaml(url);
//    }
//
//    private static ConfigurationDataSpec newSpec() {
//        return new DefaultConfigurationDataSpec();
//    }
}
