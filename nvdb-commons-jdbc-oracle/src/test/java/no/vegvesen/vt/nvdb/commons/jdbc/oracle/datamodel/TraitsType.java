package no.vegvesen.vt.nvdb.commons.jdbc.oracle.datamodel;

import no.vegvesen.vt.nvdb.commons.jdbc.oracle.ObjectType;
import no.vegvesen.vt.nvdb.commons.jdbc.oracle.ObjectTypeField;
import no.vegvesen.vt.nvdb.commons.jdbc.oracle.ObjectTypeObjectField;

import static no.vegvesen.vt.nvdb.commons.jdbc.oracle.datamodel.TestDataModel.COLOR_T;

public class TraitsType extends ObjectType {
    public ObjectTypeObjectField HAIR_COLOR;
    public ObjectTypeObjectField EYE_COLOR;
    public ObjectTypeField HEIGHT;

    TraitsType() {
        super("TRAITS");

        HAIR_COLOR = objectField("HAIR_COLOR", COLOR_T);
        EYE_COLOR  = objectField("EYE_COLOR", COLOR_T);
        HEIGHT     = scalarField("HEIGHT");
    }
}
