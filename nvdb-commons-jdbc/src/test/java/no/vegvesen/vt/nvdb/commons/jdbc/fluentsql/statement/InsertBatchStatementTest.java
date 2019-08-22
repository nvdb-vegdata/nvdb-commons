package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Context;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.MySqlDialect;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.domainmodel.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static java.util.Arrays.asList;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.datamodel.TestDataModel.PERSON;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.statement.Statements.insertBatch;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class InsertBatchStatementTest {
    private PreparableStatement statement;

    @Before
    public void setup() {
        Collection<Person> persons = asList(
                new Person(1, "Ole", "11111111111", Optional.of("123 45 678")),
                new Person(2, "Dole", "22222222222", Optional.empty()),
                new Person(3, "Doffen", "33333333333", Optional.of("345 67 890")));

        statement =
            insertBatch(persons).into(PERSON)
                .value(PERSON.ID, Person::getId)
                .value(PERSON.NAME, Person::getName)
                .value(PERSON.SSN, Person::getSsn)
                .value(PERSON.PHONE_NO, Person::getPhoneNo);
    }

    @Test
    public void shouldBuildValidSql() {
        final String expectedSql =
                "insert into PERSON (ID, NAME, SSN, PHONE_NO) "
              + "values (?, ?, ?, ?), (?, ?, ?, ?), (?, ?, ?, ?)";

        assertThat(statement.sql(context()), equalTo(expectedSql));
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        Object[] expectedArgs = {1, "Ole", "11111111111", "123 45 678", 2, "Dole", "22222222222", null, 3, "Doffen", "33333333333", "345 67 890"};
        assertThat(statement.params(), contains(expectedArgs));
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
