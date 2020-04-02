package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect;

import java.util.EnumSet;
import java.util.Optional;

import static no.vegvesen.vt.nvdb.commons.core.lang.StringHelper.generate;

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
    public String getToNumberFunction(String operand, int precision, int scale) {
        StringBuilder mask = new StringBuilder();
        if (precision-scale > 0) {
            mask.append(generate("9", precision-scale));
        }
        if (scale > 0) {
            mask.append(".").append(generate("9", scale));
        }

        return "to_number(" + operand + ", '" + mask + "')";
    }

    @Override
    public boolean supports(Capability capability) {
        return supportedCaps.contains(capability);
    }
}
