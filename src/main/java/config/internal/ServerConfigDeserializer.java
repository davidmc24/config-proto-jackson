package config.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import ratpack.launch.ServerConfig;
import ratpack.launch.ServerConfigBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.nio.file.Paths;

public class ServerConfigDeserializer extends JsonDeserializer<ServerConfig> {
    @Override
    public ServerConfig deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = jp.getCodec();
        ObjectNode serverNode = jp.readValueAsTree();
        ServerConfigBuilder builder = builderForBasedir(serverNode, ctxt);
        if (serverNode.hasNonNull("port")) {
            builder.port(serverNode.get("port").asInt());
        }
        if (serverNode.hasNonNull("address")) {
            builder.address(codec.treeToValue(serverNode.get("address"), InetAddress.class));
        }
        if (serverNode.hasNonNull("development")) {
            builder.development(serverNode.get("development").asBoolean());
        }
        if (serverNode.hasNonNull("threads")) {
            builder.threads(serverNode.get("threads").asInt());
        }
        if (serverNode.hasNonNull("publicAddress")) {
            builder.publicAddress(codec.treeToValue(serverNode.get("publicAddress"), URI.class));
        }
        if (serverNode.hasNonNull("maxContentLength")) {
            builder.maxContentLength(serverNode.get("maxContentLength").asInt());
        }
        if (serverNode.hasNonNull("timeResponses")) {
            builder.timeResponses(serverNode.get("timeResponses").asBoolean());
        }
        if (serverNode.hasNonNull("compressResponses")) {
            builder.compressResponses(serverNode.get("compressResponses").asBoolean());
        }
        if (serverNode.hasNonNull("compressionMinSize")) {
            builder.compressionMinSize(serverNode.get("compressionMinSize").asLong());
        }
        if (serverNode.hasNonNull("compressionMimeTypeWhiteList")) {
            builder.compressionWhiteListMimeTypes(toList(codec, serverNode.get("compressionMimeTypeWhiteList")));
        }
        if (serverNode.hasNonNull("compressionMimeTypeBlackList")) {
            builder.compressionBlackListMimeTypes(toList(codec, serverNode.get("compressionMimeTypeBlackList")));
        }
        if (serverNode.hasNonNull("indexFiles")) {
            builder.indexFiles(toList(codec, serverNode.get("indexFiles")));
        }
        if (serverNode.hasNonNull("ssl")) {
            builder.ssl(codec.treeToValue(serverNode.get("ssl"), SSLContext.class));
        }
        if (serverNode.hasNonNull("other")) {
            builder.other(toMap(codec, serverNode.get("other")));
        }
        return builder.build();
    }

    private static ServerConfigBuilder builderForBasedir(ObjectNode serverNode, DeserializationContext ctxt) throws IOException {
        JsonNode baseDirNode = serverNode.get("baseDir");
        if (baseDirNode == null) {
            return ServerConfigBuilder.noBaseDir();
        } else if (baseDirNode.isTextual()) {
            return ServerConfigBuilder.baseDir(Paths.get(baseDirNode.asText()));
        }
        throw ctxt.mappingException(ServerConfig.class, baseDirNode.asToken());
    }

    @SuppressWarnings("unchecked")
    private static ImmutableList<String> toList(ObjectCodec codec, JsonNode node) throws IOException {
        return codec.treeToValue(node, ImmutableList.class);
    }

    @SuppressWarnings("unchecked")
    private static ImmutableMap<String, String> toMap(ObjectCodec codec, JsonNode node) throws IOException {
        return codec.treeToValue(node, ImmutableMap.class);
    }
}
