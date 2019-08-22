package no.vegvesen.vt.nvdb.commons.core.io;

import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.isNull;
import static no.vegvesen.vt.nvdb.commons.core.io.StreamHelper.asString;

public final class ResourceHelper {
    private ResourceHelper() {}

    public static InputStream getResource(String name) throws IOException {
        InputStream stream = ResourceHelper.class.getClassLoader().getResourceAsStream(name);
        if (isNull(stream)) {
            throw new IOException("Resource " + name + " not found");
        }
        return stream;
    }

    public static InputStream getClassResource(String name, Class cls) throws IOException {
        InputStream stream = cls.getResourceAsStream(name);
        if (isNull(stream)) {
            throw new IOException("Resource " + name + " not found");
        }
        return stream;
    }

    public static String getResourceAsString(String name) throws IOException {
        try (InputStream stream = getResource(name)) {
            return asString(stream);
        }
    }

    public static String getClassResourceAsString(String name, Class cls) throws IOException {
        try (InputStream stream = getClassResource(name, cls)) {
            return asString(stream);
        }
    }
}
