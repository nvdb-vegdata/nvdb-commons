package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect;

import java.util.EnumSet;
import java.util.Optional;

public class OracleDialect implements Dialect {
    private EnumSet<Capability> supportedCaps = EnumSet.noneOf(Capability.class);

    @Override
    public String getProductName() {
        return "Oracle";
    }

    @Override
    public Optional<String> getRowNumLiteral() {
        return Optional.of("rownum");
    }

    @Override
    public boolean supports(Capability capability) {
        return supportedCaps.contains(capability);
    }
}
