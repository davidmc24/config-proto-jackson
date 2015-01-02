package config.internal;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import ratpack.func.Function;
import ratpack.func.Pair;
import ratpack.util.ExceptionUtils;

import java.util.List;
import java.util.Map;

public class EnvironmentVariablesConfigurationSource extends FlatToNestedConfigurationSource {
    public static final String DEFAULT_PREFIX = "ratpack_";

    private final ImmutableMap<String, String> data;

    public EnvironmentVariablesConfigurationSource() {
        this(DEFAULT_PREFIX, System.getenv());
    }

    public EnvironmentVariablesConfigurationSource(String prefix, Map<String, String> data) {
        super(prefix);
        this.data = ImmutableMap.copyOf(data);
    }

    @Override
    Iterable<Pair<String, String>> loadRawData() {
        return Iterables.transform(data.entrySet(), entry -> Pair.of(entry.getKey(), entry.getValue()));
    }

    @Override
    Function<String, Iterable<String>> getKeyTokenizer() {
        return splitByDelimiter("_");
    }

    @Override
    protected Function<Iterable<Pair<String, String>>, Iterable<Pair<String, String>>> transformData() {
        List<Pair<String, String>> globalEntries = Lists.newLinkedList();
        // Special handling for PORT environment variable to support common usage in PAAS systems.
        String globalPort = data.get("PORT");
        if (!Strings.isNullOrEmpty(globalPort)) {
            globalEntries.add(Pair.of("port", globalPort));
        }
        try {
            return super.transformData().andThen(localEntries -> Iterables.concat(globalEntries, localEntries));
        } catch (Exception ex) {
            throw ExceptionUtils.uncheck(ex);
        }
    }
}
