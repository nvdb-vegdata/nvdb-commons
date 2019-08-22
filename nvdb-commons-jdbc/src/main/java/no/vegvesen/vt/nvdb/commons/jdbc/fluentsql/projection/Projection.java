package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Sql;

public interface Projection extends Sql {
    Projection as(String alias);

    String alias();
}
