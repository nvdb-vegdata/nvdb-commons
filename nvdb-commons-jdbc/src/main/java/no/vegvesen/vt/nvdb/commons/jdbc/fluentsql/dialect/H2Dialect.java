package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.dialect;

import no.vegvesen.vt.nvdb.commons.core.lang.StringHelper;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static no.vegvesen.vt.nvdb.commons.core.lang.StringHelper.generate;
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
