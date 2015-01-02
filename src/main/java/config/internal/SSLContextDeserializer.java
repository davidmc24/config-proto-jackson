package config.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ratpack.ssl.SSLContexts;
import ratpack.util.ExceptionUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SSLContextDeserializer extends JsonDeserializer<SSLContext> {
    @Override
    public SSLContext deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        // TODO: add a signature to SSLContexts for Path objects
        ObjectNode node = jp.readValueAsTree();
        try {
            String keyStorePath = node.path("keyStorePath").asText();
            String keyStorePassword = node.path("keyStorePassword").asText();
            return SSLContexts.sslContext(new File(keyStorePath), keyStorePassword);
        } catch (GeneralSecurityException ex) {
            throw ExceptionUtils.uncheck(ex);
        }
    }
}
