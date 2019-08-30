package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Eq;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.EqField;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.EqSubQuery;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Expression;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Ge;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.GeField;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Gt;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.GtField;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.In;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.IsNull;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Le;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.LeField;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Like;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Lt;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.LtField;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Ascending;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Descending;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Order;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.SelectStatement;

import java.util.Collection;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.SELECT;

public class Field implements Projection {
    private final Table table;
    private final String name;
    private String alias;

    public Field(Table table, String name) {
        this.table = requireNonNull(table, "No table specified");
        this.name = requireNonBlank(name, "No name specified");
        this.alias = defaultAlias(table, name);
    }

    @Override
    public Projection as(String alias) {
        this.alias = requireNonBlank(alias, "No alias specified");
        return this;
    }

    public Expression eq(Object value) {
        return new Eq(this, requireNonNull(value, "No value specified"));
    }

    public Optional<Expression> eq(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return value.map(v -> new Eq(this, v));
    }

    public Expression eq(Field other) {
        return new EqField(this, requireNonNull(other, "No field specified"));
    }

    public Expression eq(SelectStatement subQuery) {
        return new EqSubQuery(this, requireNonNull(subQuery, "No subquery specified"));
    }

    public Expression lt(Object value) {
        return new Lt(this, requireNonNull(value, "No value specified"));
    }

    public Optional<Expression> lt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return value.map(v -> new Lt(this, v));
    }

    public Expression lt(Field other) {
        return new LtField(this, requireNonNull(other, "No field specified"));
    }

    public Expression le(Object value) {
        return new Le(this, requireNonNull(value, "No value specified"));
    }

    public Optional<Expression> le(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return value.map(v -> new Le(this, v));
    }

    public Expression le(Field other) {
        return new LeField(this, requireNonNull(other, "No field specified"));
    }

    public Expression gt(Object value) {
        return new Gt(this, requireNonNull(value, "No value specified"));
    }

    public Optional<Expression> gt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return value.map(v -> new Gt(this, v));
    }

    public Expression gt(Field other) {
        return new GtField(this, requireNonNull(other, "No field specified"));
    }

    public Expression ge(Object value) {
        return new Ge(this, requireNonNull(value, "No value specified"));
    }

    public Optional<Expression> ge(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return value.map(v -> new Ge(this, v));
    }

    public Expression ge(Field other) {
        return new GeField(this, requireNonNull(other, "No field specified"));
    }

    public Expression in(Object... values) {
        requireNonEmpty(values, "No values specified");
        if (values.length == 1) {
            return new Eq(this, values[0]);
        } else {
            return new In(this, asList(values));
        }
    }

    public Expression in(Collection<?> values) {
        requireNonEmpty(values, "No values specified");
        if (values.size() == 1) {
            return new Eq(this, values.iterator().next());
        } else {
            return new In(this, values);
        }
    }

    public Optional<Expression> in(Optional<? extends Collection<?>> values) {
        requireNonNull(values, "No values specified");
        return values.map(v -> {
            if (v.size() == 1) {
                return new Eq(this, v.iterator().next());
            } else {
                return new In(this, v);
            }
        });
    }

    public Expression like(String pattern) {
        return new Like(this, requireNonNull(pattern, "No pattern specified"));
    }

    public Optional<Expression> like(Optional<String> pattern) {
        requireNonNull(pattern, "No pattern specified");
        return pattern.map(p -> new Like(this, p));
    }

    public Expression isNull() {
        return new IsNull(this);
    }

    public Join on(Field other) {
        return new Join(this, other);
    }

    public Order asc() {
        return new Ascending(this);
    }

    public Order desc() {
        return new Descending(this);
    }

    public String name() {
        return name;
    }

    @Override
    public String alias() {
        return alias;
    }

    public Table table() {
        return table;
    }

    @Override
    public String sql(Context context) {
        if (context.isCommand(SELECT)) {
            return table.alias() + "." + name;
        } else {
            return name;
        }
    }

    private String defaultAlias(Table table, String name) {
        return (table.alias() + "_" + name).toUpperCase();
    }
}
