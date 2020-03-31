package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Sql;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.SelectStatement;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.subquery.Subquery;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;

public interface LeftOperand extends Sql {
    Stream<Field> fields();

    default Expression eq(Object value) {
        return new Eq(this, requireNonNull(value, "No value specified"));
    }

    default OptionalExpression eq(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalExpression.ofNullable(value.map(v -> new Eq(this, v)).orElse(null));
    }

    default Expression eq(Field other) {
        return new EqField(this, requireNonNull(other, "No field specified"));
    }

    default Expression eq(SelectStatement inner) {
        requireNonNull(inner, "No subquery specified");
        return new EqSubquery(this, new Subquery(inner));
    }

    default Expression lt(Object value) {
        return new Lt(this, requireNonNull(value, "No value specified"));
    }

    default OptionalExpression lt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalExpression.ofNullable(value.map(v -> new Lt(this, v)).orElse(null));
    }

    default Expression lt(Field other) {
        return new LtField(this, requireNonNull(other, "No field specified"));
    }

    default Expression le(Object value) {
        return new Le(this, requireNonNull(value, "No value specified"));
    }

    default OptionalExpression le(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalExpression.ofNullable(value.map(v -> new Le(this, v)).orElse(null));
    }

    default Expression le(Field other) {
        return new LeField(this, requireNonNull(other, "No field specified"));
    }

    default Expression gt(Object value) {
        return new Gt(this, requireNonNull(value, "No value specified"));
    }

    default OptionalExpression gt(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalExpression.ofNullable(value.map(v -> new Gt(this, v)).orElse(null));
    }

    default Expression gt(Field other) {
        return new GtField(this, requireNonNull(other, "No field specified"));
    }

    default Expression ge(Object value) {
        return new Ge(this, requireNonNull(value, "No value specified"));
    }

    default OptionalExpression ge(Optional<?> value) {
        requireNonNull(value, "No value specified");
        return OptionalExpression.ofNullable(value.map(v -> new Ge(this, v)).orElse(null));
    }

    default Expression ge(Field other) {
        return new GeField(this, requireNonNull(other, "No field specified"));
    }

    default Expression in(Object... values) {
        requireNonEmpty(values, "No values specified");
        if (values.length == 1) {
            return new Eq(this, values[0]);
        } else {
            return new In(this, asList(values));
        }
    }

    default Expression in(Collection<?> values) {
        requireNonEmpty(values, "No values specified");
        if (values.size() == 1) {
            return new Eq(this, values.iterator().next());
        } else {
            return new In(this, values);
        }
    }

    default OptionalExpression in(Optional<? extends Collection<?>> values) {
        requireNonNull(values, "No values specified");
        return OptionalExpression.ofNullable(values.map(v -> {
            if (v.size() == 1) {
                return new Eq(this, v.iterator().next());
            } else {
                return new In(this, v);
            }
        }).orElse(null));
    }

    default Expression like(String pattern) {
        return new Like(this, requireNonNull(pattern, "No pattern specified"));
    }

    default OptionalExpression like(Optional<String> pattern) {
        requireNonNull(pattern, "No pattern specified");
        return OptionalExpression.ofNullable(pattern.map(p -> new Like(this, p)).orElse(null));
    }
}
