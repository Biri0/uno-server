package it.rfmariano.uno_server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Player {
    private final List<Card> cards;

    public Player() {
        this.cards = new ArrayList<>();
    }

    public int handSize() {
        return this.cards.size();
    }

    public void drawCard(Card card) {
        cards.add(card);
    }

    public Card playCard(int position) {
        if (position < 0 || position >= cards.size())
            throw new IllegalArgumentException("value must be between 0 and " + (cards.size() - 1));
        return cards.remove(position);
    }

    public List<Card> viewHand() {
        return Collections.unmodifiableList(cards);
    }

    public List<Integer> getPlayableIndexes(Card topCard, CardColor chosenColor) {
        Objects.requireNonNull(topCard, "topCard");
        List<Integer> playable = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (isPlayable(topCard, chosenColor, cards.get(i))) {
                playable.add(i);
            }
        }
        return playable;
    }

    public List<Card> getPlayableCards(Card topCard, CardColor chosenColor) {
        List<Integer> indexes = getPlayableIndexes(topCard, chosenColor);
        List<Card> playable = new ArrayList<>(indexes.size());
        for (int i : indexes) {
            playable.add(cards.get(i));
        }
        return playable;
    }

    private static boolean isPlayable(Card topCard, CardColor chosenColor, Card candidate) {
        if (candidate instanceof WildCard) {
            return true;
        }

        CardColor colorToMatch = (topCard instanceof ColoredCard cc)
                ? cc.color()
                : Objects.requireNonNull(chosenColor, "chosenColor");

        if (candidate instanceof ColoredCard col) {
            if (col.color() == colorToMatch) {
                return true;
            }
            if (candidate instanceof NumberCard nc && topCard instanceof NumberCard tn && nc.value() == tn.value()) {
                return true;
            }
            if (candidate instanceof ActionCard ac && topCard instanceof ActionCard ta && ac.action() == ta.action()) {
                return true;
            }
        }
        return false;
    }
}
