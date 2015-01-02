package config

import ratpack.launch.ServerConfig

class YamlConfigurationSpec extends BaseConfigurationSpec {
    def "supports yaml"() {
        def baseDir = tempFolder.newFolder("baseDir").toPath()
        def keyStoreFile = tempFolder.newFile("keystore.jks").toPath()
        def keyStorePassword = "changeit"
        createKeystore(keyStoreFile, keyStorePassword)
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
}
