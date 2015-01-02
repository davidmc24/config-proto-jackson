package config

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Path
import java.security.KeyStore

abstract class BaseConfigurationSpec extends Specification {
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

    protected static void createKeystore(Path path, String password) {
        def ks = KeyStore.getInstance("JKS")
        ks.load(null, password.toCharArray())
        path.withOutputStream { ks.store(it, password.toCharArray()) }
    }
}
