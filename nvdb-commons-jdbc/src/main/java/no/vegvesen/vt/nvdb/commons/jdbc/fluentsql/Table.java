package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql;

import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Command.SELECT;

public class Table {
    private final String name;
    private String alias;

    protected Table(String name) {
        this.name = requireNonBlank(name, "No name specified");
        this.alias = defaultAlias(name);
    }

    protected Table(String name, String alias) {
        this.name = requireNonBlank(name, "No name specified");
        this.alias = requireNonBlank(alias, "No name specified");
    }

    public Table as(String alias) {
        return new Table(name, alias);
    }

    public Field field(String name) {
        return new Field(this, name);
    }

    public String name() {
        return name;
    }

    public String alias() {
        return alias;
    }

    public String sql(Context context) {
        if (context.isCommand(SELECT)) {
            return name + " " + alias;
        } else {
            return name;
        }
    }

    private String defaultAlias(String name) {
        return name;
    }
}
