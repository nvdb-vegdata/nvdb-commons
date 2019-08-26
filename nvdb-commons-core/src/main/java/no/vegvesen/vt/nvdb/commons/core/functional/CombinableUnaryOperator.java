package no.vegvesen.vt.nvdb.commons.core.functional;

import java.util.function.UnaryOperator;

public interface CombinableUnaryOperator<T> extends UnaryOperator<T> {
    static <T> CombinableUnaryOperator<T> of(UnaryOperator<T> first) {
        return first::apply;
    }

    default CombinableUnaryOperator<T> andThen(UnaryOperator<T> after) {
        return value -> after.apply(this.apply(value));
    }
}
