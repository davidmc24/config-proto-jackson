package config

import config.internal.EnvironmentVariablesConfigurationSource
import ratpack.launch.ServerConfig
import spock.lang.Unroll

class EnvVarConfigurationSpec extends BaseConfigurationSpec {
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
}
