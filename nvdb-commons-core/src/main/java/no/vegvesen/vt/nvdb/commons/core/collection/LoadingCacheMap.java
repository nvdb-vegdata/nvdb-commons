package no.vegvesen.vt.nvdb.commons.core.collection;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

public class LoadingCacheMap<K, V> {
    private final Map<K, LoadingCache<V>> map;

    private final Duration refreshInterval;
    private final Function<K, V> loader;
    private final String valueDescription;

    public LoadingCacheMap(int maxSize, Duration refreshInterval, Function<K, V> loader, String valueDescription) {
        this.refreshInterval = refreshInterval;
        this.loader = loader;
        this.map = new CacheMap<>(maxSize);
        this.valueDescription = valueDescription;
    }

    public synchronized V get(K key) {
        if (!map.containsKey(key)) {
            LoadingCache<V> value = new LoadingCache<>(
                valueDescription + " for " + key,
                refreshInterval,
                () -> loader.apply(key)
            );

            map.put(key, value);
            return value.get();
        } else {
            return map.get(key).get();
        }
    }

    public synchronized void invalidate(K key) {
        map.remove(key);
    }
}
