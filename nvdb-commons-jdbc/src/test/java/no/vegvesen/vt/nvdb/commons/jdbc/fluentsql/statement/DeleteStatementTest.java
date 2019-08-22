package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.MySqlDialect;
import org.junit.Before;
import org.junit.Test;

import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.datamodel.TestDataModel.PERSON;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.Statements.delete;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.Statements.deleteFrom;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class DeleteStatementTest {
    private PreparableStatement statement;

    @Before
    public void setup() {
        statement =
            delete().from(PERSON)
                .where(PERSON.SSN.eq("17016812345"), PERSON.NAME.isNull());
    }

    @Test
    public void shouldBuildValidSql() {
        final String expectedSql =
                "delete from PERSON "
              + "where SSN = ? "
              + "and NAME is null";

        assertThat(statement.sql(context()), equalTo(expectedSql));
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        Object[] expectedArgs = {"17016812345"};
        assertThat(statement.params(), contains(expectedArgs));
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
