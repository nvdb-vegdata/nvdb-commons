package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.datamodel;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;

public class AddressTable extends Table {
    public Field PERSON_ID;
    public Field STREET;
    public Field ZIP;
    public Field COUNTRY;
    public Field VERIFIED;

    AddressTable() {
        super("ADDRESS", "A");

        PERSON_ID = field("PERSON_ID");
        STREET = field("STREET");
        ZIP = field("ZIP");
        COUNTRY = field("COUNTRY");
        VERIFIED = field("VERIFIED");
    }
}
