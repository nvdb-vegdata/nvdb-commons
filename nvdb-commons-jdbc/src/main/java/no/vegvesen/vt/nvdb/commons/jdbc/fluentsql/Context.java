package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.Dialect;

public class Context {
    private final Dialect dialect;
    public Command command;

    public static Context of(Dialect dialect) {
        return new Context(dialect);
    }

    private Context(Dialect dialect) {
        this.dialect = dialect;
    }

    public void command(Command command) {
        this.command = command;
    }

    public Dialect getDialect() {
        return this.dialect;
    }

    public boolean isCommand(Command command) {
        return this.command == command;
    }
}