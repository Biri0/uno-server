package it.rfmariano.uno_server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "deck")
public class Deck {
    @Id
    private UUID id;
    @org.hibernate.annotations.Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<Card> cards;

    public Deck() {
        id = UuidCreator.getTimeOrderedEpoch();
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
        this.cards = new ArrayList<>(ordered);
    }

    public void load(List<Card> cards) {
        Collections.shuffle(cards);
        this.cards = new ArrayList<>(cards);
    }

    public int size() {
        return cards.size();
    }

    public Card draw() {
        return cards.removeFirst();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
