package no.vegvesen.vt.nvdb.commons.core.lang;

import java.util.Optional;

import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;

public class EnvVar {
    private final String prefix;

    public static EnvVar withPrefix(String prefix) {
        requireNonBlank(prefix, "prefix is blank");
        return new EnvVar(prefix);
    }

    public static EnvVar withoutPrefix() {
        return new EnvVar("");
    }

    private EnvVar(String prefix) {
        this.prefix = prefix;
    }

    public Optional<Integer> getInt(String name) {
        return lookupVar(name).map(Integer::parseInt);
    }

    public int getInt(String name, int defaultValue) {
        return getInt(name).orElse(defaultValue);
    }

    public Optional<Boolean> getBoolean(String name) {
        return lookupVar(name).map(Boolean::parseBoolean);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return getBoolean(name).orElse(defaultValue);
    }

    public Optional<String> getString(String name) {
        return lookupVar(name);
    }

    public String getString(String name, String defaultValue) {
        return getString(name).orElse(defaultValue);
    }

    public String getFullKey(String name) {
        return (prefix + name).toUpperCase();
    }

    private Optional<String> lookupVar(String name) {
        requireNonBlank(name, "name is blank");
        String key = getFullKey(name);
        return Optional.ofNullable(System.getenv(key));
    }
}
