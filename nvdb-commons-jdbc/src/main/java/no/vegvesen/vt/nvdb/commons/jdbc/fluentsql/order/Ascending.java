package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Ascending implements Order {
    private Projection projection;

    public Ascending(Projection projection) {
        this.projection = requireNonNull(projection, "No projection specified");
    }

    @Override                                      // ??? field or alias ???
    public String sql(Context context) {
        return projection.sql(context) + " asc";
    }

    @Override
    public Stream<Field> fields() {
        return projection instanceof Field ? Stream.of((Field)projection) : Stream.empty();
    }
}
