package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.Dialect;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.MySqlDialect;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

public abstract class PreparableStatement {
    abstract String sql(Context context);
    abstract List<Object> params();

    public String toString(Dialect dialect) {
        String stringifiedParams = params().stream()
                .map(Objects::toString)
                .collect(joining(", "));

        return sql(Context.of(dialect)) + (stringifiedParams.isEmpty() ? " with no params" : " with params " + stringifiedParams);
    }

    @Override
    public String toString() {
        return toString(new MySqlDialect());
    }
}
