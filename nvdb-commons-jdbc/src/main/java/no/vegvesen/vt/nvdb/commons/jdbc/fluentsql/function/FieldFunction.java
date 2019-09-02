package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Order;

import java.util.Optional;

public interface FieldFunction extends Function {
    // One aggregate function, count(*), does not operate on a specific field
    Optional<Field> field();

    default Order ascIf(boolean condition) {
        return field().get().ascIf(condition);
    }

    default Order asc() {
        return field().get().asc();
    }

    default Order desc() {
        return field().get().desc();
    }
}
