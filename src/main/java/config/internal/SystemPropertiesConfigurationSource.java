package config.internal;

public final class SystemPropertiesConfigurationSource extends PropertiesConfigurationSource {
    public SystemPropertiesConfigurationSource() {
        super(System.getProperties());
    }
}
