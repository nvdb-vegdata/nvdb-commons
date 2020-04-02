package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.singlerow;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.require;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;

public class ToNumber implements SingleRowFunction {
    private final Field field;
    private final int precision;
    private final int scale;
    private String alias;

    public ToNumber(Field field, int precision, int scale) {
        require(() -> precision >= 1, "precision must be 1 or greater");
        require(() -> scale >= 0, "scale must be 0 or greater");
        require(() -> scale <= precision, "scale must be less than or equal precision");

        this.field = requireNonNull(field, "No field specified");
        this.alias = defaultAlias(field);
        this.precision = precision;
        this.scale = scale;
    }

    @Override
    public Optional<Field> field() {
        return Optional.of(field);
    }

    @Override
    public Projection as(String alias) {
        this.alias = requireNonBlank(alias, "No alias specified");
        return this;
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return context.getDialect().getToNumberFunction(field.sql(context), precision, scale);
    }

    private String defaultAlias(Field field) {
        return "TO_NUMBER_" + field.alias();
    }
}
