package no.vegvesen.vt.nvdb.commons.core.lang;

import java.util.Optional;

import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;

public class SysProp {
    private final String prefix;
    
    public static SysProp withPrefix(String prefix) {
        requireNonBlank(prefix, "prefix is blank");
        return new SysProp(prefix); 
    }

    public static SysProp withoutPrefix() {
        return new SysProp(""); 
    }
    
    private SysProp(String prefix) {
        this.prefix = prefix;
    }

    public Optional<Integer> getInt(String name) {
        return lookupProp(name).map(Integer::parseInt);
    }

    public int getInt(String name, int defaultValue) {
        return getInt(name).orElse(defaultValue);
    }

    public Optional<Boolean> getBoolean(String name) {
        return lookupProp(name).map(Boolean::parseBoolean);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return getBoolean(name).orElse(defaultValue);
    }

    public Optional<String> getString(String name) {
        return lookupProp(name);
    }

    public String getString(String name, String defaultValue) {
        return getString(name).orElse(defaultValue);
    }

    public String getFullKey(String name) {
        return (prefix + name);
    }

    private Optional<String> lookupProp(String name) {
        requireNonBlank(name, "name is blank");
        String key = getFullKey(name);
        return Optional.ofNullable(System.getProperty(key));
    }
}
