package no.vegvesen.vt.nvdb.commons.core.collection;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class LoadingCache<T> {
    private final Duration refreshInterval;
    private final Supplier<T> supplier;

    private Instant expiryTime;
    private T cache;

    public LoadingCache(Duration refreshInterval, Supplier<T> supplier) {
        this.refreshInterval = refreshInterval;
        this.supplier = supplier;
    }

    public synchronized T get() {
        if (needRefresh()) {
            cache = supplier.get();
            expiryTime = Instant.now().plus(refreshInterval);
        }

        return cache;
    }

    public synchronized void invalidate() {
        this.cache = null;
    }

    private boolean needRefresh() {
        return cache == null || (refreshInterval != Duration.ZERO && Instant.now().isAfter(expiryTime));
    }
}
