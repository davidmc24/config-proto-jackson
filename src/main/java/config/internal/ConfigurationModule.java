package config.internal;

import com.fasterxml.jackson.databind.module.SimpleModule;
import ratpack.launch.ServerConfig;

import javax.net.ssl.SSLContext;

public class ConfigurationModule extends SimpleModule {
    public ConfigurationModule() {
        super("ratpack-configuration");
        addDeserializer(ServerConfig.class, new ServerConfigDeserializer());
        addDeserializer(SSLContext.class, new SSLContextDeserializer());
    }
}
