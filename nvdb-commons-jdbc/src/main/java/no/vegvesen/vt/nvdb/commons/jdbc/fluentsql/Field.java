package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Eq;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.EqField;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.EqSubquery;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Expression;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Ge;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.GeField;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Gt;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.GtField;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.In;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.IsNull;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Le;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.LeField;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.LeftOperand;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Like;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Lt;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.LtField;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.OptionalExpression;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Ascending;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Descending;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Order;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.SelectStatement;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.subquery.Subquery;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.SELECT;

public class Field implements Projection, LeftOperand {
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

    @Override
    public Stream<Field> fields() {
        return Stream.of(this);
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

    public Order ascIf(boolean condition) {
        return condition ? asc() : desc();
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
