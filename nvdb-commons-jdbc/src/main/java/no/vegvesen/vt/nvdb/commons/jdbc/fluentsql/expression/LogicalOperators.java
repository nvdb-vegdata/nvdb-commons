package no.vegvesen.vt.nvdb.commons.jdbc.fluentsql.expression;

public abstract class LogicalOperators {
    public static And and(Expression... operands) {
        return new And(operands);
    }

    public static Or or(Expression... operands) {
        return new Or(operands);
    }

    public static Not not(Expression operand) {
        return new Not(operand);
    }
}
