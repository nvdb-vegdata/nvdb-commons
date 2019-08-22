package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.MySqlDialect;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

public abstract class PreparableStatement {
    abstract String sql(Context context);
    abstract List<Object> params();

    @Override
    public String toString() {
        String stringifiedParams = params().stream()
                .map(Objects::toString)
                .collect(joining(", "));

        return sql(Context.of(new MySqlDialect())) + (stringifiedParams.isEmpty() ? " with no params" : " with params " + stringifiedParams);
    }
}
