package config;

public interface ConfigurationData {
    <O> O get(String pointer, Class<O> type);
    default <O> O get(Class<O> type) {
        return get(null, type);
    }
}
