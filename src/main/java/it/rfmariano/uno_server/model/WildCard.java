package it.rfmariano.uno_server.model;

import java.util.Objects;

public record WildCard(WildType type) implements Card {
    public WildCard {
        Objects.requireNonNull(type, "type");
    }
}
