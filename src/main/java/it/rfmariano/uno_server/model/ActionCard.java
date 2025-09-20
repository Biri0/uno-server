package it.rfmariano.uno_server.model;

import java.util.Objects;

public record ActionCard(CardColor color, ActionType action) implements ColoredCard {
    public ActionCard {
        Objects.requireNonNull(color, "color");
        Objects.requireNonNull(action, "action");
    }

    @Override
    public boolean isPlayableOver(Card topCard, CardColor chosenColor) {
        CardColor match = (topCard instanceof ColoredCard cc) ? cc.color()
                : Objects.requireNonNull(chosenColor, "chosenColor");
        return this.color() == match || (topCard instanceof ActionCard tn && this.action() == tn.action());
    }
}
