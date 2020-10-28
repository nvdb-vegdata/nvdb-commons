package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.Expression;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.IsNull;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.LeftOperand;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Ascending;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Descending;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Order;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.SELECT;

public class Field implements Projection, LeftOperand {
    private final Table table;
    private final String name;
    private final String alias;

    public Field(Table table, String name) {
        this.table = requireNonNull(table, "No table specified");
        this.name = requireNonBlank(name, "No name specified");
        this.alias = defaultAlias(table, name);
    }

    private Field(Table table, String name, String alias) {
        this.table = requireNonNull(table, "No table specified");
        this.name = requireNonBlank(name, "No name specified");
        this.alias = requireNonBlank(alias, "No alias specified");
    }

    @Override
    public Projection as(String alias) {
        return new Field(table, name, alias);
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
