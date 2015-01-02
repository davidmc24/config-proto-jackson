package config

import ratpack.launch.ServerConfig

class PropsConfigurationSpec extends BaseConfigurationSpec {
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
}
