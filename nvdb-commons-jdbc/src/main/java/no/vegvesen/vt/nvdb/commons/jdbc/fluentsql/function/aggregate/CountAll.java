package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.aggregate;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Order;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

import java.util.Optional;

import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;

public class CountAll implements AggregateFunction {
    private String alias;

    public CountAll() {
        this.alias = defaultAlias();
    }

    @Override
    public Projection as(String alias) {
        this.alias = requireNonBlank(alias, "No alias specified");
        return this;
    }

    @Override
    public Optional<Field> field() {
        return Optional.empty();
    }

    @Override
    public Order ascIf(boolean condition) {
        throw new UnsupportedOperationException("The 'count(*)' function can't be used in an 'order by' clause");
    }

    @Override
    public Order asc() {
        throw new UnsupportedOperationException("The 'count(*)' function can't be used in an 'order by' clause");
    }

    @Override
    public Order desc() {
        throw new UnsupportedOperationException("The 'count(*)' function can't be used in an 'order by' clause");
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String sql(Context context) {
        return "count(*)";
    }

    private String defaultAlias() {
        return "COUNT_ALL";
    }
}
