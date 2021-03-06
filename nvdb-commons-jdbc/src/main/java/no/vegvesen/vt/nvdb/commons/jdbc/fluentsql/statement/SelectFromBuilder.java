package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.projection.Projection;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.subquery.Subquery;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static no.vegvesen.vt.nvdb.commons.core.collection.CollectionHelper.asNonEmptyList;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.require;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;

public class SelectFromBuilder {
    private boolean distinct;
    private List<Projection> projections;

    SelectFromBuilder(boolean distinct, List<Projection> projections) {
        this.distinct = distinct;
        this.projections = requireNonEmpty(projections, "No projections specified");

        require(() -> !this.projections.contains(null), "Use nullValue() to provide an SQL NULL projection");
    }

    public SelectFromBuilder distinct() {
        this.distinct = true;
        return this;
    }

    public SelectStatement from(Table firstTable, Table... moreTables) {
        requireNonNull(firstTable, "First table is null");
        List<Table> tables = asNonEmptyList(firstTable, moreTables);
        return new SelectStatement(distinct, projections, tables);
    }

    public SelectStatement from(SelectStatement inner) {
        requireNonNull(inner, "No select statement specified");
        return new SelectStatement(distinct, projections, new Subquery(inner));
    }

    public SelectStatement from(SelectStatement inner, String alias) {
        requireNonNull(inner, "No select statement specified");
        requireNonBlank(alias, "No alias specified");
        return new SelectStatement(distinct, projections, new Subquery(inner).as(alias));
    }
}
