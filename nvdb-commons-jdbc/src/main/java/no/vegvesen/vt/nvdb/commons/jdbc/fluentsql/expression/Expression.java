package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Sql;

import java.util.Optional;
import java.util.stream.Stream;

public interface Expression extends Sql {
    String negatedSql(Context context);

    Stream<Field> fields();

    Stream<Object> params();

    default Expression or(Expression other) {
        return LogicalOperators.or(this, other);
    }

    default OptionalExpression or(OptionalExpression maybeOther) {
        if (maybeOther.isPresent()) {
            return OptionalExpression.of(LogicalOperators.or(this, maybeOther.get()));
        } else {
            return OptionalExpression.of(this);
        }
    }

    default Expression and(Expression other) {
        return LogicalOperators.and(this, other);
    }

    default OptionalExpression and(OptionalExpression maybeOther) {
        if (maybeOther.isPresent()) {
            return OptionalExpression.of(LogicalOperators.and(this, maybeOther.get()));
        } else {
            return OptionalExpression.of(this);
        }
    }
}
