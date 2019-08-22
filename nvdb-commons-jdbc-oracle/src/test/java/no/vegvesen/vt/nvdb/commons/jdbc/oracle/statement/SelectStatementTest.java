package no.vegvesen.vt.nvdb.commons.jdbc.oracle.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.OracleDialect;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.PreparableStatement;
import org.junit.Before;
import org.junit.Test;

import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.Statements.select;
import static no.vegvesen.vt.nvdb.commons.jdbc.oracle.datamodel.TestDataModel.TRAITS;
import static no.vegvesen.vt.nvdb.commons.jdbc.oracle.datamodel.TestDataModel.TRAITS_T;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class SelectStatementTest {

    @Before
    public void setup() {

    }

    @Test
    public void shouldOracleTreatFunction() {
        /*PreparableStatement statement =
                select(treat(value(TRAITS).as(TRAITS_T)).as("T"))
                        .from(TRAITS)
                        .where(PERSON.SSN.eq("17016812345"));

        final String expectedSql =
                "select max(A.ZIP) MAX_ZIP "
                        + "from ADDRESS A "
                        + "inner join PERSON P on A.PERSON_ID = P.ID "
                        + "where P.SSN = ?";
        Object[] expectedParams = {"17016812345"};

        assertThat(statement.sql(context()), equalTo(expectedSql));
        assertThat(statement.params(), contains(expectedParams));*/
    }

    private Context context() {
        return Context.of(new OracleDialect());
    }
}
