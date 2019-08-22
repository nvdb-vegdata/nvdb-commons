package no.vegvesen.vt.nvdb.commons.core.contract;

@FunctionalInterface
public interface Requirement {
    boolean test();
}
