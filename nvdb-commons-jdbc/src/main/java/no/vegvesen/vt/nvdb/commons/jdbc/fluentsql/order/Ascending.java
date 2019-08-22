package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Ascending implements Order {
    private Field field;

    public Ascending(Field field) {
        this.field = requireNonNull(field, "No field specified");
    }

    @Override
    public String sql(Context context) {
        return field.sql(context) + " asc";
    }

    @Override
    public Stream<Field> fields() {
        return Stream.of(field);
    }
}
