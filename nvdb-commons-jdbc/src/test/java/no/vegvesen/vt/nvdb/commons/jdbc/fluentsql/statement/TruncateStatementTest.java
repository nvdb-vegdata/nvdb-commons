package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.MySqlDialect;
import org.junit.Before;
import org.junit.Test;

import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.datamodel.TestDataModel.PERSON;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.Statements.truncate;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class TruncateStatementTest {
    private PreparableStatement statement;

    @Before
    public void setup() {
        statement =
            truncate().table(PERSON);
    }

    @Test
    public void shouldBuildValidSql() {
        final String expectedSql =
                "truncate table PERSON";

        assertThat(statement.sql(context()), equalTo(expectedSql));
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        assertThat(statement.params(), empty());
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
