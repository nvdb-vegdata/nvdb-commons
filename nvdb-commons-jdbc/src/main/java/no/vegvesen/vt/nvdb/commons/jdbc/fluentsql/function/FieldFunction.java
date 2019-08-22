package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.Function;

public interface FieldFunction extends Function {
    Field field();
}
