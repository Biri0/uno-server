package it.rfmariano.uno_server.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Deck {
    private Deque<Card> cards;

    public Deck() {
        List<Card> ordered = new ArrayList<>(108);

        for (CardColor color : CardColor.values()) {
            ordered.add(new NumberCard(color, 0));
            for (int n = 1; n <= 9; n++) {
                ordered.add(new NumberCard(color, n));
                ordered.add(new NumberCard(color, n));
            }
            for (ActionType a : ActionType.values()) {
                ordered.add(new ActionCard(color, a));
                ordered.add(new ActionCard(color, a));
            }
        }

        for (int i = 0; i < 4; i++) {
            ordered.add(new WildCard(WildType.WILD));
            ordered.add(new WildCard(WildType.WILD_DRAW_FOUR));
        }

        Collections.shuffle(ordered);
        this.cards = new ArrayDeque<>(ordered);
    }

    public void load(List<Card> cards) {
        Collections.shuffle(cards);
        this.cards = new ArrayDeque<>(cards);
    }

    public int size() {
        return cards.size();
    }

    public Card draw() {
        return cards.pollFirst();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
