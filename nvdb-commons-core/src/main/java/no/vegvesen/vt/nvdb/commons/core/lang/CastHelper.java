package no.vegvesen.vt.nvdb.commons.core.lang;

public class CastHelper {
    private CastHelper() {}

    /**
     * Casts specified object to specified type if it is instance of that type, else returns null.
     * @param object the object to cast
     * @param clazz the class object of the type to cast to
     * @param <T> the type to cast to
     * @return the object cast to specified type if possible; if not, null
     */
    public static <T> T castOrNull(Object object, Class<T> clazz) {
        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        } else {
            return null;
        }
    }
}
