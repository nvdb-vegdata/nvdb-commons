package no.vegvesen.vt.nvdb.commons.core.lang;

public class MathHelper {
    public final static double EPSILON = 0.00001;

    /**
     * Compares two double values using an epsilon value of {@value #EPSILON}.
     * @return true if a is greater than b.
     */
    public static boolean greaterThan(double a, double b){
        return a - b > EPSILON;
    }

    /**
     * Compares two double values using an epsilon value of {@value #EPSILON}.
     * @return true if a is less than b.
     */
    public static boolean lessThan(double a, double b) {
        return Double.compare(a, b) != 0 && a - b < EPSILON;
    }
}
