package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.internal.*;
import ratpack.func.Action;

import java.net.URL;
import java.nio.file.Path;
import java.util.Properties;

public interface ConfigurationDataSpec {
    // TODO: add stuff back in

    ConfigurationDataSpec configureObjectMapper(Action<ObjectMapper> action);

    ConfigurationDataSpec add(ConfigurationSource configurationSource);

    ConfigurationData build();

//    ConfigurationDataSpec env();

    ConfigurationDataSpec json(Path path);

//    ConfigurationDataSpec json(URL url);
//
    ConfigurationDataSpec props(Path path);

//    ConfigurationDataSpec props(Properties properties);
//
//    ConfigurationDataSpec props(URL url);
//
//    ConfigurationDataSpec sysProps();
//
    ConfigurationDataSpec yaml(Path path);

//    ConfigurationDataSpec yaml(URL url);
}
