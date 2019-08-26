package no.vegvesen.vt.nvdb.commons.core.functional;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Command {
    static Command noOp() {
        return () -> {};
    }

    void execute();

    default Command then(Command next) {
        requireNonNull(next, "No next command specified");
        return () -> {
            this.execute();
            next.execute();
        };
    }
}
