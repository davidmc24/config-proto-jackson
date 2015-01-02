package config

import com.zaxxer.hikari.HikariConfig
import config.internal.EnvironmentVariablesConfigurationSource
import ratpack.launch.ServerConfig

class ConfigurationUsageSpec extends BaseConfigurationSpec {
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

    def "can combine configuration from multiple sources"() {
        def jsonFile = tempFolder.newFile("file.json").toPath()
        jsonFile.text = '{"port": 8080}'
        def propsFile = tempFolder.newFile("file.properties").toPath()
        propsFile.text = 'development=true'
        def yamlFile = tempFolder.newFile("file.yaml").toPath()
        yamlFile.text = 'publicAddress: http://localhost:8080'
        System.setProperty("ratpack.threads", "3")
        def envData = [ratpack_address: "localhost"]

        when:
        def envSource = new EnvironmentVariablesConfigurationSource(EnvironmentVariablesConfigurationSource.DEFAULT_PREFIX, envData)
        def serverConfig = Configurations.config().json(jsonFile).yaml(yamlFile).props(propsFile).add(envSource).sysProps().build().get(ServerConfig)

        then:
        serverConfig.port == 8080
        serverConfig.address == InetAddress.getByName("localhost")
        serverConfig.development
        serverConfig.threads == 3
        serverConfig.publicAddress == URI.create("http://localhost:8080")
        serverConfig.maxContentLength == ServerConfig.DEFAULT_MAX_CONTENT_LENGTH
    }

    def "can get objects from subpaths"() {
        def yamlFile = tempFolder.newFile("file.yaml").toPath()
yamlFile.text = """
server:
    port: 7654
hikari:
    jdbcUrl: "jdbc:h2:mem:"
"""
        when:
        def config = Configurations.config().yaml(yamlFile).build()
        def serverConfig = config.get("/server", ServerConfig)
        def hikariConfig = config.get("/hikari", HikariConfig)

        then:
        serverConfig.port == 7654
        hikariConfig.jdbcUrl == "jdbc:h2:mem:"
    }
}