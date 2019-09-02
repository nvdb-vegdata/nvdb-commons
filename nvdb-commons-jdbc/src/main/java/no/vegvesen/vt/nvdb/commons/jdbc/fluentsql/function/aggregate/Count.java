package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.aggregate;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;

public class Count implements AggregateFunction {
    private final Field field;
    private String alias;

    public Count(Field field) {
        this.field = requireNonNull(field, "No field specified");
        this.alias = defaultAlias(field);
    }

    @Override
    public Projection as(String alias) {
        this.alias = requireNonBlank(alias, "No alias specified");
        return this;
    }

    @Override
    public Optional<Field> field() {
        return Optional.of(field);
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "count(" + field.sql(context) + ")";
    }

    private String defaultAlias(Field field) {
        return "COUNT_" + field.alias();
    }
}
