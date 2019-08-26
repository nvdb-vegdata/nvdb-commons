package no.vegvesen.vt.nvdb.commons.core.lang;

import org.apache.commons.math3.util.Precision;

import java.util.Comparator;

import static java.util.Objects.requireNonNull;

/**
 * Comparator of double values that allows some deviation (typically caused by IEEE754 anomalies).
 */
public class DoubleComparator implements Comparator<Double> {
    private final double epsilon;

    public static DoubleComparator comparingWithEpsilon(double epsilon) {
        return new DoubleComparator(epsilon);
    }

    public DoubleComparator(double epsilon) {
        this.epsilon = epsilon;
    }

    public boolean lessThan(double o1, double o2) {
        return compare(o1, o2) < 0;
    }

    public boolean lessThanOrEqual(double o1, double o2) {
        return compare(o1, o2) <= 0;
    }

    public boolean equal(double o1, double o2) {
        return compare(o1, o2) == 0;
    }

    public boolean greaterThanOrEqual(double o1, double o2) {
        return compare(o1, o2) >= 0;
    }

    public boolean greaterThan(double o1, double o2) {
        return compare(o1, o2) > 0;
    }

    @Override
    public int compare(Double o1, Double o2) {
        requireNonNull(o1, "first argument can't be null");
        requireNonNull(o2, "second argument can't be null");
        return Precision.compareTo(o1, o2, epsilon);
    }
}
