package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.constant;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;

public interface Constant extends Projection {
    Constant forField(Field field);
}
