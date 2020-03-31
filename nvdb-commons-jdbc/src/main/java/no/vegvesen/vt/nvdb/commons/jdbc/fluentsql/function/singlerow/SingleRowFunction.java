package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.singlerow;

import no.vegvesen.vt.nvdb.commons.core.functional.Optionals;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression.LeftOperand;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.FieldFunction;

import java.util.stream.Stream;

public interface SingleRowFunction extends FieldFunction, LeftOperand {
    @Override
    default Stream<Field> fields() {
        return Optionals.stream(field());
    }
}
