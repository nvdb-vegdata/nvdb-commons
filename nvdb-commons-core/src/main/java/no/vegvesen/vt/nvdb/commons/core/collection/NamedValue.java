package no.vegvesen.vt.nvdb.commons.core.collection;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class NamedValue<V> {
    private final String name;
    private final V value;

    public static <V> NamedValue of(String name, V value) {
        return new NamedValue<>(name, value);
    }

    private NamedValue(String name, V value) {
        this.name = requireNonNull(name, "name can't be null");
        this.value = requireNonNull(value, "value can't be null");
    }

    public String getName() {
        return name;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedValue namedValue = (NamedValue) o;
        return Objects.equals(name, namedValue.name) &&
                Objects.equals(value, namedValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
