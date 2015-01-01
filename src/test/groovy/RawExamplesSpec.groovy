import spock.lang.Ignore

@Ignore
class RawExamplesSpec {
    def "John Engelman 2014-12-31 5:22pm"() {
//        ServerConfig serverConfig = Configuration.env().sysprops().build()
//        HikariConfig hikariConfig = HikariConfiguration.env().sysprops().build()
    }

    def "John Engelman 2014-12-31 5:19pm"() {
//        RatpackLauncher.launcher { r ->
//          Configuration config = ConfigurationSource.env().sysprops().build()
//          r.add(ServiceConfig, config.get(ServerConfig))
//          r.add(HikariConfig, HikariConfig.get(config))
//        }
    }

    def "Dan Woods 2014-12-31 5:18pm"() {
//        config().add(props("file.properties")).add(yaml("file.yaml")).add(json("file.json")).env().sysProps().toClass(ServerConfig.class)
    }

    def "Dan Woods 2014-12-30 5:36pm"() {
//        config().add(propFile(props1)).add(propFile(props2)).get(DbRegistry)`
//
//        Dan Woods [5:37 PM]
//        could similarly have `yaml()`, `json()`, etc
//
//        Dan Woods [5:38 PM]
//        `config().add(yaml("foo.yml")).get(ServerConfig)`
//
//        Dan Woods [5:40 PM]
//        `config().add(propFile("app.properties")).add(yaml(file("/etc/app/config.yml"))).get(ServerConfig)
    }

    def "John Engelman sometime"() {
//        might be the way to go. Curious to hear everyones thoughts tonight.
//
//        John Engelman [9:34 AM]
//        Also, for the builder pattern above, could that be supported through that Discovery interface?
//
//        John Engelman [9:35 AM]
//        i.e there's a `json(file)` method, that throws an exception unless it can discover a provider for it

//        Well you could do something like `Configurations.load(yaml(file))` where `yaml(file)` is a static method on a custom class and it returns a `Function`
//
//        John Engelman [9:37 AM]
//        Actually probably returns `Action` since you have no inputs
    }

    def "dcarr before that"() {
//        If we're breaking it into `ratpack-config`, `ratpack-config-yaml`, `ratpack-config-json`, etc., that seems to me like it isn't a good fit for the envisioned `ConfigurationSource.env().sysProps().yaml("blah.yaml").build()` builder approach.  Perhaps introduce a `ConfigurationDataSource` interface that each of the config data type modules can provide implementations of, and then support `ConfigurationSource.load(env()).load(sysProps()).load(yaml("blah.yaml").build()`.  In this model, `env/sysProps/yaml/json` might be static builder methods provided as a convenience to eliminate the need to directly construct SystemProperties/Environment/Yaml/Json-ConfigurationDataSource.
    }

    def "luke"() {
//        def configSource = ConfigurationSource.env().sysProps().yaml("blah.yaml").build()
//        def dbSettings = configSource.get(MyDbSettings.class)
//        def cacheSettings = configSource.get(MyConfigSettings.class)
//        ```
//        (edited)
//
//        Luke Daley [5:21 PM]
//        This will be required so we can make this work well with `ConfigurableModule`.
//
//        Luke Daley [5:21 PM]5:21
//        We'll want a way for the module config object to be supplied via external config.
    }

    def dan() {
//        configuration.toClass(MyConfig.class)
//        configuration.toClass(VendorConfig.class)
//        configuration.toClass(SpringBullshit.class)
    }

    def "dan and john"() {
//        Dan Woods [2:36 PM]
//        but i would probably explicitly map app config to one class, server config to another class
//
//        Dan Woods [2:36 PM]
//        right
//
//        Dan Woods [2:36 PM]
//        but you _could_
//
//        Dan Woods [2:36 PM]
//        if you're really into DRY :simple_smile:
//
//        John Engelman [2:36 PM]
//        what if there was `Configuration.getServerConfig()`?
//
//        Dan Woods [2:37 PM]
//        `Configurations.load("app.properties").toServerConfig()` ?
//
//        John Engelman [2:37 PM]
//        then we provide something like:
//        ```void register(Configuration configuration) {
//          registry.add(ServerConfig, configuration.getServerConfig())
//          registry.add(configuration.getType(), configuration.toType())
//        }
//        ```
//
//        John Engelman [2:38 PM]
//        that's not perfect, but gives you an idea
    }
}
