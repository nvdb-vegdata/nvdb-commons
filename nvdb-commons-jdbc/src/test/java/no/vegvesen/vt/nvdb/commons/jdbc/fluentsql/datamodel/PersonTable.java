package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.datamodel;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;

public class PersonTable extends Table {
    public Field ID;
    public Field NAME;
    public Field SSN;
    public Field PHONE_NO;

    PersonTable() {
        super("PERSON", "P");

        ID = field("ID");
        NAME = field("NAME");
        SSN = field("SSN");
        PHONE_NO = field("PHONE_NO");
    }
}
