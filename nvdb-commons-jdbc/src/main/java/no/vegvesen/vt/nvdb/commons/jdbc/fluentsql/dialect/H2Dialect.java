package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect;

import java.util.EnumSet;
import java.util.Optional;

import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.Capability.LIMIT_OFFSET;

public class H2Dialect implements Dialect {
    private EnumSet<Capability> supportedCaps = EnumSet.of(LIMIT_OFFSET);

    @Override
    public String getProductName() {
        return "H2";
    }

    @Override
    public Optional<String> getRowNumLiteral() {
        return Optional.of("rownum()");
    }

    @Override
    public boolean supports(Capability capability) {
        return supportedCaps.contains(capability);
    }
}
