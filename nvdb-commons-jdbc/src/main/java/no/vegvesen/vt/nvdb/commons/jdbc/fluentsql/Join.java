package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.require;

public class Join {
    private enum JoinMode {
        INNER("inner join"), LEFT_OUTER("left outer join"), RIGHT_OUTER("right outer join");

        final String sql;

        JoinMode(String sql) {
            this.sql = sql;
        }
    }

    private final List<Field> lefts = new ArrayList<>();
    private final List<Field> rights = new ArrayList<>();
    private JoinMode mode;

    public Join(Field left, Field right) {
        this.lefts.add(requireNonNull(left, "No left field specified"));
        this.rights.add(requireNonNull(right, "No right field specified"));
        this.mode = JoinMode.INNER;
    }

    private Join(Collection<Field> lefts, Collection<Field> rights, JoinMode mode) {
        this.lefts.addAll(lefts);
        this.rights.addAll(rights);
        this.mode = mode;
    }

    public Join leftOuter() {
        this.mode = JoinMode.LEFT_OUTER;
        return this;
    }

    public Join rightOuter() {
        this.mode = JoinMode.RIGHT_OUTER;
        return this;
    }

    public Join and(Join next) {
        require(() -> this.lefts.get(0).table().equals(next.lefts.get(0).table()), "Left side of nested joins must belong to the same table");
        require(() -> this.rights.get(0).table().equals(next.rights.get(0).table()), "Right side of nested joins must belong to the same table");

        Collection<Field> lefts = new ArrayList<>(this.lefts);
        lefts.addAll(next.lefts);
        Collection<Field> rights = new ArrayList<>(this.rights);
        rights.addAll(next.rights);

        return new Join(lefts, rights, mode);
    }

    public String sql(Context context) {
        Table rightTable = this.rights.get(0).table();
        StringBuilder sql = new StringBuilder()
            .append(mode.sql)
            .append(" ")
            .append(rightTable.sql(context))
            .append(" on ");

        for (int i = 0; i < this.lefts.size(); i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append(this.lefts.get(i).sql(context))
                .append(" = ")
                .append(this.rights.get(i).sql(context));
        }

        return sql.toString();
    }

    public Table joined() {
        return rights.get(0).table();
    }

    public Stream<Field> fields() {
        return Stream.concat(lefts.stream(), rights.stream());
    }
}
