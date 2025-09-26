package it.rfmariano.uno_server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.Type;

import com.github.f4b6a3.uuid.UuidCreator;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "player")
public class Player {
    @Id
    private UUID id;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private final List<Card> cards;

    public Player() {
        this.id = UuidCreator.getTimeOrderedEpoch();
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
            if (cards.get(i).isPlayableOver(topCard, chosenColor)) {
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
}
