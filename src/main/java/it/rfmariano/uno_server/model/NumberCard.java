package it.rfmariano.uno_server.model;

import java.util.Objects;

public record NumberCard(CardColor color, int value) implements ColoredCard {
    public NumberCard {
        Objects.requireNonNull(color, "color");
        if (value < 0 || value > 9)
            throw new IllegalArgumentException("value must be between 0 and 9");
    }

    @Override
    public boolean isPlayableOver(Card topCard, CardColor chosenColor) {
        CardColor match = (topCard instanceof ColoredCard cc) ? cc.color()
                : Objects.requireNonNull(chosenColor, "chosenColor");
        return this.color() == match || (topCard instanceof NumberCard tn && this.value() == tn.value());
    }
}
