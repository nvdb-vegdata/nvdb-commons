package no.vegvesen.vt.nvdb.commons.core.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class LoadingCache<T> {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoadingCache.class);

    private final String name;
    private final Duration refreshInterval;
    private final Supplier<T> supplier;

    private Instant expiryTime;
    private T cache;

    public LoadingCache(String name, Duration refreshInterval, Supplier<T> supplier) {
        this.name = name;
        this.refreshInterval = refreshInterval;
        this.supplier = supplier;
    }

    public synchronized T get() {
        if (needRefresh()) {
            LOGGER.info("Initiating refresh of {}", name);
            cache = supplier.get();
            expiryTime = Instant.now().plus(refreshInterval);
            LOGGER.info("Finished refresh of {}", name);
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
