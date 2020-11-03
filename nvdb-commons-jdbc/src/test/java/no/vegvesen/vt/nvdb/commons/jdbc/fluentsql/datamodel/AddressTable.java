package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.datamodel;

import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Field;
import no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.Table;

public class AddressTable extends Table<AddressTable> {
    public Field PERSON_ID = field("PERSON_ID");
    public Field STREET = field("STREET");
    public Field ZIP = field("ZIP");
    public Field COUNTRY = field("COUNTRY");
    public Field VERIFIED = field("VERIFIED");

    AddressTable(String alias) {
        super("ADDRESS", alias, AddressTable::new);
    }

    AddressTable() {
        super("ADDRESS", "A", AddressTable::new);

        //PERSON_ID = field("PERSON_ID");
        //STREET = field("STREET");
        //ZIP = field("ZIP");
        //COUNTRY = field("COUNTRY");
        //VERIFIED = field("VERIFIED");
    }
}
