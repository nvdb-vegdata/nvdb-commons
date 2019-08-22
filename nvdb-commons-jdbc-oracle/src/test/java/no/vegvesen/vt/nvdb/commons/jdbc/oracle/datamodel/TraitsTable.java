package no.vegvesen.vt.nvdb.commons.jdbc.oracle.datamodel;

import no.vegvesen.vt.nvdb.commons.jdbc.oracle.ObjectTable;
import no.vegvesen.vt.nvdb.commons.jdbc.oracle.ObjectTypeField;
import no.vegvesen.vt.nvdb.commons.jdbc.oracle.ObjectTypeObjectField;

public class TraitsTable extends ObjectTable {
    public ObjectTypeField ID;
    public ObjectTypeObjectField TRAITS;

    TraitsTable() {
        super("TRAITS", TestDataModel.TRAITS_T);

        ID = scalarField("ID");
        TRAITS = objectField("TRAITS", TestDataModel.TRAITS_T);
    }
}
