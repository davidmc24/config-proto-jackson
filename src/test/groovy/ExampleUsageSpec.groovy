import config.Configurations
import config.internal.EnvironmentVariablesConfigurationSource
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import ratpack.launch.ServerConfig
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Path
import java.security.KeyStore

class ExampleUsageSpec extends Specification {
    // TODO: remove
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
//    def "source overriding"() {
//        // TODO
//    }
//
//    // TODO: support loading files from classpath... maybe by supporting URI/URL, or prefixes
//
//    def "ability to get raw data?"() {
//        // TODO... do we even want this?
//    }
}
