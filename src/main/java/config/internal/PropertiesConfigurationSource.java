package config.internal;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import ratpack.func.Function;
import ratpack.func.Pair;
import ratpack.util.ExceptionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesConfigurationSource extends FlatToNestedConfigurationSource {
    private final Properties properties;

    public PropertiesConfigurationSource(String prefix, Properties properties) {
        super(prefix);
        this.properties = properties;
        // TODO: pull in richer impl from https://github.com/danveloper/config-binding/blob/master/src/main/java/config/PropertiesConfigurationSource.java
    }

    public PropertiesConfigurationSource(String prefix, ByteSource byteSource) {
        this(prefix, load(byteSource));
    }

    public PropertiesConfigurationSource(URL url) {
        this(null, Resources.asByteSource(url));
    }

    public PropertiesConfigurationSource(Path path) {
        this(null, Files.asByteSource(path.toFile()));
    }

    @Override
    Iterable<Pair<String, String>> loadRawData() {
        return Iterables.transform(properties.stringPropertyNames(), key -> Pair.of(key, properties.getProperty(key)));
    }

    @Override
    Function<String, Iterable<String>> getKeyTokenizer() {
        return splitByDelimiter(".");
    }

    private static Properties load(ByteSource byteSource) {
        Properties properties = new Properties();
        try (InputStream inputStream = byteSource.openStream()) {
            properties.load(inputStream);
        } catch (IOException ex) {
            throw ExceptionUtils.uncheck(ex);
        }
        return properties;
    }
}
