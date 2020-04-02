package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect;

import java.util.EnumSet;
import java.util.Optional;

import static no.vegvesen.vt.nvdb.commons.core.lang.StringHelper.generate;
import static no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect.Capability.LIMIT_OFFSET;

public class PostgreSqlDialect implements Dialect {
    private EnumSet<Capability> supportedCaps = EnumSet.of(LIMIT_OFFSET);

    @Override
    public String getProductName() {
        return "PostgreSQL";
    }

    @Override
    public Optional<String> getRowNumLiteral() {
        return Optional.empty();
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
