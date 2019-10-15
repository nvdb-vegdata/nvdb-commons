package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Ascending;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Descending;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.order.Order;
import sun.security.krb5.internal.crypto.Des;

import java.util.Optional;

public interface FieldFunction extends Function {
    // One aggregate function, count(*), does not operate on a specific field
    Optional<Field> field();

    default Order ascIf(boolean condition) {
        return condition ? asc() : desc();
    }

    default Order asc() {
        return new Ascending(this);
    }

    default Order desc() {
        return new Descending(this);
    }
}
