package it.rfmariano.uno_server.model;

import java.util.Objects;

public record ActionCard(CardColor color, ActionType action) implements ColoredCard {
    public ActionCard {
        Objects.requireNonNull(color, "color");
        Objects.requireNonNull(action, "action");
    }
}
