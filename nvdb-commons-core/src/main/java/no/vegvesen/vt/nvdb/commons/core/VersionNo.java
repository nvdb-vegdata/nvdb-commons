package no.vegvesen.vt.nvdb.commons.core;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonBlank;
import static no.vegvesen.vt.nvdb.commons.core.contract.Requires.requireNonEmpty;

public class VersionNo implements Comparable<VersionNo> {
    private final int[] components;

    public static VersionNo parse(String versionNo) {
        requireNonBlank(versionNo, "Version number is blank");
        int[] components = Stream.of(versionNo.split("\\.")).mapToInt(Integer::parseInt).toArray();
        return new VersionNo(components);
    }

    public static boolean isBeforeOrEqual(String firstVersionNo, String secondVersionNo) {
        return parse(firstVersionNo).compareTo(parse(secondVersionNo)) <= 0;
    }

    public VersionNo(int... components) {
        this.components = requireNonEmpty(components, "No components specified");
    }

    public boolean isBefore(VersionNo other) {
        return this.compareTo(other) < 0;
    }

    public boolean isAfter(VersionNo other) {
        return this.compareTo(other) > 0;
    }

    public String format() {
        return IntStream.of(components).mapToObj(Integer::toString).collect(joining("."));
    }

    @Override
    public int compareTo(VersionNo other) {
        requireNonNull(other, "No version number specified");

        int commonDepth = Math.min(this.components.length, other.components.length);
        for (int level=0; level<commonDepth; level++) {
            if (this.components[level] < other.components[level]) {
                return -1;
            } else if (this.components[level] > other.components[level]) {
                return +1;
            }
        }

        if (this.components.length < other.components.length) {
            return -1;
        } else if (this.components.length > other.components.length) {
            return +1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return format();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VersionNo)) return false;
        VersionNo versionNo = (VersionNo) o;
        return Arrays.equals(components, versionNo.components);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(components);
    }
}
