package config;

import com.google.common.reflect.TypeToken;

public interface ConfigurationData {
    <O> O get(Class<O> type);

//    <O> O get(TypeToken<O> type) throws ConfigurationException;

    // TODO: more?
}
