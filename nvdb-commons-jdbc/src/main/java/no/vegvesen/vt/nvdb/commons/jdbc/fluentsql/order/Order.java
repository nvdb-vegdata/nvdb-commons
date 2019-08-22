package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Sql;

import java.util.stream.Stream;

public interface Order extends Sql {
    Stream<Field> fields();
}
