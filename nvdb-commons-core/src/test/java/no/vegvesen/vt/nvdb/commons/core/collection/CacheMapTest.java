package no.vegvesen.vt.nvdb.commons.core.collection;

import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class CacheMapTest {

    @Test
    public void shouldPreserveMostRecentlyItems() {
        CacheMap<Integer, Integer> out = new CacheMap<>(2);
        out.put(1, 1);
        out.put(2, 2);
        out.put(3, 3);

        assertThat(out.values(), contains(2, 3));
    }

    @Test
    public void shouldActiveAccessedItem() {
        CacheMap<Integer, Integer> out = new CacheMap<>(2);
        out.put(1, 1);
        out.put(2, 2);
        out.get(1);
        out.put(3, 3);

        assertThat(out.values(), contains(1, 3));
    }
}
