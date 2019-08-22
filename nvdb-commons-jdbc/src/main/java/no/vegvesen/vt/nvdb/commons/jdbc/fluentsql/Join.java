package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Join {
    private enum JoinMode {
        INNER("inner join"), LEFT_OUTER("left outer join"), RIGHT_OUTER("right outer join");

        final String sql;

        JoinMode(String sql) {
            this.sql = sql;
        }
    }

    private final Field left;
    private final Field right;
    private JoinMode mode;

    public Join(Field left, Field right) {
        this.left = requireNonNull(left, "No left field specified");
        this.right = requireNonNull(right, "No right field specified");
        this.mode = JoinMode.INNER;
    }

    public Join leftOuter() {
        this.mode = JoinMode.LEFT_OUTER;
        return this;
    }

    public Join rightOuter() {
        this.mode = JoinMode.RIGHT_OUTER;
        return this;
    }

    public String sql(Context context) {
        return mode.sql + " " + right.table().sql(context) + " on " + left.sql(context) + " = " + right.sql(context);
    }

    public Table joined() {
        return right.table();
    }

    public Stream<Field> fields() {
        return Stream.of(left, right);
    }
}
