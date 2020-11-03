package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.datamodel;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;

public class PersonTable extends Table<PersonTable> {
    public Field ID = field("ID");
    public Field NAME = field("NAME");
    public Field SSN = field("SSN");
    public Field PHONE_NO = field("PHONE_NO");

    PersonTable(String alias) {
        super("PERSON", alias, PersonTable::new);
    }
    PersonTable() {
        super("PERSON", "P", PersonTable::new);

        //ID = field("ID");
        //NAME = field("NAME");
        //SSN = field("SSN");
        //PHONE_NO = field("PHONE_NO");
    }
}
