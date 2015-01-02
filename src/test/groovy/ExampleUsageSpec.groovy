import config.Configurations
import config.internal.EnvironmentVariablesConfigurationSource
import jdk.nashorn.internal.runtime.regexp.joni.Config
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import ratpack.launch.ServerConfig
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Path
import java.nio.file.Paths
import java.security.KeyStore

class ExampleUsageSpec extends Specification {
    @Rule
    TemporaryFolder tempFolder

    Properties origSysProps

    def setup() {
        origSysProps = System.properties
        System.properties = new Properties(origSysProps)
    }

    def cleanup() {
        System.properties = origSysProps
    }

    def "properly loads ServerConfig defaults"() {
        when:
        def serverConfig = Configurations.config().build().get(ServerConfig)

        then:
        !serverConfig.hasBaseDir
        serverConfig.port == ServerConfig.DEFAULT_PORT
        !serverConfig.address
        !serverConfig.development
        serverConfig.threads == ServerConfig.DEFAULT_THREADS
        !serverConfig.publicAddress
        serverConfig.maxContentLength == ServerConfig.DEFAULT_MAX_CONTENT_LENGTH
        !serverConfig.timeResponses
        !serverConfig.compressResponses
        serverConfig.compressionMinSize == ServerConfig.DEFAULT_COMPRESSION_MIN_SIZE
        serverConfig.compressionMimeTypeWhiteList == [] as Set
        serverConfig.compressionMimeTypeBlackList == [] as Set
        serverConfig.indexFiles == []
        !serverConfig.SSLContext
        serverConfig.getOtherPrefixedWith("") == [:]
    }

    def "supports json"() {
        def baseDir = tempFolder.newFolder("baseDir").toPath()
        def keyStoreFile = tempFolder.newFile("keystore.jks").toPath()
        def keyStorePassword = "changeit"
        createKeystore(keyStoreFile, keyStorePassword)
        def configFile = tempFolder.newFile("file.json").toPath()
        configFile.text =
"""
{
    "baseDir": "${baseDir.toString()}",
    "port": 8080,
    "address": "localhost",
    "development": true,
    "threads": 3,
    "publicAddress": "http://localhost:8080",
    "maxContentLength": 50000,
    "timeResponses": true,
    "compressResponses": true,
    "compressionMinSize": 100,
    "compressionMimeTypeWhiteList": ["application/json", "text/plain"],
    "compressionMimeTypeBlackList": ["image/png", "image/gif"],
    "indexFiles": ["index.html", "index.htm"],
    "ssl": {
        "keyStorePath": "${keyStoreFile.toString()}",
        "keyStorePassword": "${keyStorePassword}"
    },
    "other": {
        "a": "1",
        "b": "2"
    }
}
"""

        when:
        def serverConfig = Configurations.config().json(configFile).build().get(ServerConfig)

        then:
        serverConfig.hasBaseDir
        serverConfig.baseDir.file == baseDir
        serverConfig.port == 8080
        serverConfig.address == InetAddress.getByName("localhost")
        serverConfig.development
        serverConfig.threads == 3
        serverConfig.publicAddress == URI.create("http://localhost:8080")
        serverConfig.maxContentLength == 50000
        serverConfig.timeResponses
        serverConfig.compressResponses
        serverConfig.compressionMinSize == 100
        serverConfig.compressionMimeTypeWhiteList == ["application/json", "text/plain"] as Set
        serverConfig.compressionMimeTypeBlackList == ["image/png", "image/gif"] as Set
        serverConfig.indexFiles == ["index.html", "index.htm"]
        serverConfig.SSLContext
        serverConfig.getOtherPrefixedWith("") == [a:"1", b:"2"]
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Unroll
    def "support PORT environment variable: #envData to #expectedPort"() {
        when:
        def envSource = new EnvironmentVariablesConfigurationSource(EnvironmentVariablesConfigurationSource.DEFAULT_PREFIX, envData)
        def serverConfig = Configurations.config().add(envSource).build().get(ServerConfig)

        then:
        serverConfig.port == expectedPort

        where:
        expectedPort | envData
        5432         | [PORT: "5432"]
        8080         | [PORT: "5432", ratpack_port: "8080"]
        8080         | [ratpack_port: "8080"]
    }

    def "supports environment variables"() {
        def baseDir = tempFolder.newFolder("baseDir").toPath()
        def keyStoreFile = tempFolder.newFile("keystore.jks").toPath()
        def keyStorePassword = "changeit"
        createKeystore(keyStoreFile, keyStorePassword)
        def envData = [
                ratpack_baseDir: baseDir.toString(),
                ratpack_port: "8080",
                ratpack_address: "localhost",
                ratpack_development: "true",
                ratpack_threads: "3",
                ratpack_publicAddress: "http://localhost:8080",
                ratpack_maxContentLength: "50000",
                ratpack_timeResponses: "true",
                ratpack_compressResponses: "true",
                ratpack_compressionMinSize: "100",
                ratpack_ssl_keyStorePath: keyStoreFile.toString(),
                ratpack_ssl_keyStorePassword: keyStorePassword,
                ratpack_other_a: "1",
                ratpack_other_b: "2",
        ]

        when:
        def envSource = new EnvironmentVariablesConfigurationSource(EnvironmentVariablesConfigurationSource.DEFAULT_PREFIX, envData)
        def serverConfig = Configurations.config().add(envSource).build().get(ServerConfig)

        then:
        serverConfig.hasBaseDir
        serverConfig.baseDir.file == baseDir
        serverConfig.port == 8080
        serverConfig.address == InetAddress.getByName("localhost")
        serverConfig.development
        serverConfig.threads == 3
        serverConfig.publicAddress == URI.create("http://localhost:8080")
        serverConfig.maxContentLength == 50000
        serverConfig.timeResponses
        serverConfig.compressResponses
        serverConfig.compressionMinSize == 100
        // TODO: support for lists
//        serverConfig.compressionMimeTypeWhiteList == ["application/json", "text/plain"] as Set
//        serverConfig.compressionMimeTypeBlackList == ["image/png", "image/gif"] as Set
//        serverConfig.indexFiles == ["index.html", "index.htm"]
        serverConfig.SSLContext
        serverConfig.getOtherPrefixedWith("") == [a:"1", b:"2"]
    }

    def "supports properties"() {
        def baseDir = tempFolder.newFolder("baseDir").toPath()
        def keyStoreFile = tempFolder.newFile("keystore.jks").toPath()
        def keyStorePassword = "changeit"
        createKeystore(keyStoreFile, keyStorePassword)
        def configFile = tempFolder.newFile("file.properties").toPath()
        configFile.text =
"""
# This is a comment
baseDir: ${baseDir.toString()}
port: 8080
address: localhost
development: true
threads: 3
publicAddress: http://localhost:8080
maxContentLength: 50000
timeResponses: true
compressResponses: true
compressionMinSize: 100
ssl.keyStorePath: ${keyStoreFile.toString()}
ssl.keyStorePassword: ${keyStorePassword}
other.a: 1
other.b: 2
"""

        when:
        def serverConfig = Configurations.config().props(configFile).build().get(ServerConfig)

        then:
        serverConfig.hasBaseDir
        serverConfig.baseDir.file == baseDir
        serverConfig.port == 8080
        serverConfig.address == InetAddress.getByName("localhost")
        serverConfig.development
        serverConfig.threads == 3
        serverConfig.publicAddress == URI.create("http://localhost:8080")
        serverConfig.maxContentLength == 50000
        serverConfig.timeResponses
        serverConfig.compressResponses
        serverConfig.compressionMinSize == 100
        // TODO: support for lists
//        serverConfig.compressionMimeTypeWhiteList == ["application/json", "text/plain"] as Set
//        serverConfig.compressionMimeTypeBlackList == ["image/png", "image/gif"] as Set
//        serverConfig.indexFiles == ["index.html", "index.htm"]
        serverConfig.SSLContext
        serverConfig.getOtherPrefixedWith("") == [a:"1", b:"2"]
    }

    def "supports system properties"() {
        def baseDir = tempFolder.newFolder("baseDir").toPath()
        def keyStoreFile = tempFolder.newFile("keystore.jks").toPath()
        def keyStorePassword = "changeit"
        createKeystore(keyStoreFile, keyStorePassword)
        System.setProperty("ratpack.baseDir", baseDir.toString())
        System.setProperty("ratpack.port", "8080")
        System.setProperty("ratpack.address", "localhost")
        System.setProperty("ratpack.development", "true")
        System.setProperty("ratpack.threads", "3")
        System.setProperty("ratpack.publicAddress", "http://localhost:8080")
        System.setProperty("ratpack.maxContentLength", "50000")
        System.setProperty("ratpack.timeResponses", "true")
        System.setProperty("ratpack.compressResponses", "true")
        System.setProperty("ratpack.compressionMinSize", "100")
        System.setProperty("ratpack.ssl.keyStorePath", keyStoreFile.toString())
        System.setProperty("ratpack.ssl.keyStorePassword", keyStorePassword)
        System.setProperty("ratpack.other.a", "1")
        System.setProperty("ratpack.other.b", "2")

        when:
        def serverConfig = Configurations.config().sysProps().build().get(ServerConfig)

        then:
        serverConfig.hasBaseDir
        serverConfig.baseDir.file == baseDir
        serverConfig.port == 8080
        serverConfig.address == InetAddress.getByName("localhost")
        serverConfig.development
        serverConfig.threads == 3
        serverConfig.publicAddress == URI.create("http://localhost:8080")
        serverConfig.maxContentLength == 50000
        serverConfig.timeResponses
        serverConfig.compressResponses
        serverConfig.compressionMinSize == 100
        // TODO: support for lists
//        serverConfig.compressionMimeTypeWhiteList == ["application/json", "text/plain"] as Set
//        serverConfig.compressionMimeTypeBlackList == ["image/png", "image/gif"] as Set
//        serverConfig.indexFiles == ["index.html", "index.htm"]
        serverConfig.SSLContext
        serverConfig.getOtherPrefixedWith("") == [a:"1", b:"2"]
    }

    def "supports yaml"() {
        def baseDir = tempFolder.newFolder("baseDir").toPath()
        def keyStoreFile = tempFolder.newFile("keystore.jks").toPath()
        def keyStorePassword = "changeit"
        def configFile = tempFolder.newFile("file.yaml").toPath()
        configFile.text =
"""
---
# This is a comment
baseDir: ${baseDir.toString()}
port: 8080
address: localhost
development: true
threads: 3
publicAddress: http://localhost:8080
maxContentLength: 50000
timeResponses: true
compressResponses: true
compressionMinSize: 100
compressionMimeTypeWhiteList: ["application/json", "text/plain"]
compressionMimeTypeBlackList: ["image/png", "image/gif"]
indexFiles:
    - index.html
    - index.htm
ssl:
    keyStorePath: ${keyStoreFile.toString()}
    keyStorePassword: ${keyStorePassword}
other:
    a: "1"
    b: "2"
...
"""
        createKeystore(keyStoreFile, keyStorePassword)

        when:
        def serverConfig = Configurations.config().yaml(configFile).build().get(ServerConfig)

        then:
        serverConfig.hasBaseDir
        serverConfig.baseDir.file == baseDir
        serverConfig.port == 8080
        serverConfig.address == InetAddress.getByName("localhost")
        serverConfig.development
        serverConfig.threads == 3
        serverConfig.publicAddress == URI.create("http://localhost:8080")
        serverConfig.maxContentLength == 50000
        serverConfig.timeResponses
        serverConfig.compressResponses
        serverConfig.compressionMinSize == 100
        serverConfig.compressionMimeTypeWhiteList == ["application/json", "text/plain"] as Set
        serverConfig.compressionMimeTypeBlackList == ["image/png", "image/gif"] as Set
        serverConfig.indexFiles == ["index.html", "index.htm"]
        serverConfig.SSLContext
        serverConfig.getOtherPrefixedWith("") == [a:"1", b:"2"]
    }

//    def "basic usage"() {
//        def configurationSource = Configurations.add(props("file.properties")).add(yaml("file.yaml")).add(json("file.json")).env().sysProps()
//        def serverConfig = configurationSource.get(ServerConfig)
//        def hikariConfig = configurationSource.get(HikariConfig)
//    }
//
//    def "Supports getting config objects from subpaths"() {
//        def configurationSource = Configurations.add(props("file.properties")).add(yaml("file.yaml")).add(json("file.json")).env().sysProps()
//        def serverConfig = config
//    }
//
//    def "supports various input sources"() {
//        // Configurations.add(props("file.properties")).add(yaml("file.yaml")).add(json("file.json")).env().sysProps()
//        // TODO
//    }
//
//    def "can add custom configuration sources"() {
//        // TODO
//    }
//
//    def "source overriding"() {
//        // TODO
//    }
//
//    // TODO: support loading files from classpath... maybe by supporting URI/URL, or prefixes
//
//    def "ability to get raw data?"() {
//        // TODO... do we even want this?
//    }

    private static void createKeystore(Path path, String password) {
        def ks = KeyStore.getInstance("JKS")
        ks.load(null, password.toCharArray())
        path.withOutputStream { ks.store(it, password.toCharArray()) }
    }
}
