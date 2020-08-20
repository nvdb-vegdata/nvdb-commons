package no.vegvesen.vt.nvdb.commons.core.lang;

import java.util.function.Supplier;

import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;
import static no.vegvesen.vt.nvdb.commons.core.functional.Optionals.firstPresent;
import static no.vegvesen.vt.nvdb.commons.core.functional.Suppliers.illegalStateException;

public class Environment {
    private final EnvVar envVar;
    private final SysProp sysProp;

    public static Environment withPrefixes(String envVarPrefix, String sysPropPrefix) {
        requireNonBlank(envVarPrefix, "envVarPrefix is blank");
        requireNonBlank(sysPropPrefix, "sysPropPrefix is blank");
        return new Environment(envVarPrefix, sysPropPrefix);
    }

    public static Environment withoutPrefix() {
        return new Environment("", "");
    }

    private Environment(String envVarPrefix, String sysPropPrefix) {
        this.envVar = EnvVar.withPrefix(envVarPrefix);
        this.sysProp = SysProp.withPrefix(sysPropPrefix);
    }

    public int getInt(String name) {
        return firstPresent(envVar.getInt(name), sysProp.getInt(name)).orElseThrow(notFound(name));
    }

    public int getInt(String name, int defaultValue) {
        return firstPresent(envVar.getInt(name), sysProp.getInt(name)).orElse(defaultValue);
    }

    public boolean getBoolean(String name) {
        return firstPresent(envVar.getBoolean(name), sysProp.getBoolean(name)).orElseThrow(notFound(name));
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return firstPresent(envVar.getBoolean(name), sysProp.getBoolean(name)).orElse(defaultValue);
    }

    public String getString(String name) {
        return firstPresent(envVar.getString(name), sysProp.getString(name)).orElseThrow(notFound(name));
    }

    public String getString(String name, String defaultValue) {
        return firstPresent(envVar.getString(name), sysProp.getString(name)).orElse(defaultValue);
    }

    private Supplier<RuntimeException> notFound(String name) {
        String envVarKey = envVar.getFullKey(name);
        String sysPropKey = sysProp.getFullKey(name);
        return illegalStateException("Environment variable %s or system property %s not found", envVarKey, sysPropKey);
    }
}
