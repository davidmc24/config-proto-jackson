package config.internal;

public final class SystemPropertiesConfigurationSource extends PropertiesConfigurationSource {
    public static final String DEFAULT_PREFIX = "ratpack.";

    public SystemPropertiesConfigurationSource() {
        this(DEFAULT_PREFIX);
    }

    public SystemPropertiesConfigurationSource(String prefix) {
        super(prefix, System.getProperties());
    }
}
