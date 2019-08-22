package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.MySqlDialect;
import org.junit.Before;
import org.junit.Test;

import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.datamodel.TestDataModel.ADDRESS;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.datamodel.TestDataModel.PERSON;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.function.system.SystemFunctions.currentTimestamp;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.Statements.insert;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.Statements.insertInto;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class InsertStatementTest {
    private PreparableStatement statement;

    @Before
    public void setup() {
        statement =
            insert().into(PERSON)
                .value(PERSON.NAME, "Tore Torell")
                .value(PERSON.SSN, "17016812345");
    }

    @Test
    public void shouldBuildValidSql() {
        final String expectedSql =
                "insert into PERSON (NAME, SSN) "
              + "values (?, ?)";

        assertThat(statement.sql(context()), equalTo(expectedSql));
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        Object[] expectedArgs = {"Tore Torell", "17016812345"};
        assertThat(statement.params(), contains(expectedArgs));
    }

    @Test
    public void shouldAllowSystemFunctionAsValue() {
        PreparableStatement statement =
                insert().into(ADDRESS)
                        .value(ADDRESS.PERSON_ID, 123)
                        .value(ADDRESS.STREET, "Tryllegata 14")
                        .value(ADDRESS.ZIP, "7089")
                        .value(ADDRESS.COUNTRY, "Noreg")
                        .value(ADDRESS.VERIFIED, currentTimestamp());

        final String expectedSql =
                "insert into ADDRESS (PERSON_ID, STREET, ZIP, COUNTRY, VERIFIED) "
              + "values (?, ?, ?, ?, current_timestamp)";
        Object[] expectedParams = {123, "Tryllegata 14", "7089", "Noreg"};

        assertThat(statement.sql(context()), equalTo(expectedSql));
        assertThat(statement.params(), contains(expectedParams));
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
