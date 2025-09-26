package it.rfmariano.uno_server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.Type;

import com.github.f4b6a3.uuid.UuidCreator;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "game")
public class Game {
    @Id
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    private final Deck deck;
    @OneToMany(cascade = CascadeType.ALL)
    private final List<Player> players;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private final List<Card> discardPile;

    @Column(name = "chosen_color", nullable = true)
    private CardColor chosenColor;
    @Column(name = "winner_index", nullable = true)
    private Integer winnerIndex;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Card currentCard;
    @Column(name = "current_player_index", nullable = false)
    private int currentPlayerIndex;
    @Column(name = "direction", nullable = false)
    private int direction;

    public Game(ArrayList<Player> players) {
        id = UuidCreator.getTimeOrderedEpoch();

        this.deck = new Deck();
        this.discardPile = new ArrayList<>();
        this.players = players;

        currentPlayerIndex = 0;
        direction = 1;

        for (Player player : this.players) {
            for (int i = 0; i < 7; i++) {
                player.drawCard(deck.draw());
            }
        }
        this.currentCard = deck.draw();
    }

    public Player currentPlayer() {
        return this.players.get(this.currentPlayerIndex);
    }

    public int currentPlayerIndex() {
        return this.currentPlayerIndex;
    }

    public void play(int cardIndex, CardColor chosenColorParam) {
        if (winnerIndex != null) {
            throw new IllegalStateException("game is ended");
        }

        Card candidate = currentPlayer().viewHand().get(cardIndex);
        if (!candidate.isPlayableOver(currentCard, this.chosenColor)) {
            throw new IllegalArgumentException("card not playable");
        }

        if (this.currentCard != null) {
            discardPile.add(this.currentCard);
        }

        Card played = currentPlayer().playCard(cardIndex);
        this.currentCard = played;

        if (played instanceof ColoredCard) {
            this.chosenColor = null;
        } else if (played instanceof WildCard) {
            this.chosenColor = Objects.requireNonNull(chosenColorParam, "chosenColor");
        }

        if (currentPlayer().handSize() == 0) {
            winnerIndex = currentPlayerIndex;
        }

        if (played instanceof WildCard wc) {
            if (wc.type() == WildType.WILD_DRAW_FOUR) {
                drawCards(nextPlayer(), 4);
                advanceTurn();
            }
        } else if (played instanceof ActionCard ac) {
            switch (ac.action()) {
                case SKIP -> advanceTurn();
                case REVERSE -> {
                    this.direction = -this.direction;
                    if (players.size() == 2) {
                        advanceTurn();
                    }
                }
                case DRAW_TWO -> {
                    drawCards(nextPlayer(), 2);
                    advanceTurn();
                }
            }
        }

        advanceTurn();
    }

    private void drawCards(int playerIndex, int cards) {
        int idx = Math.floorMod(playerIndex, players.size());
        for (int i = 0; i < cards; i++) {
            if (deck.isEmpty()) {
                deck.load(new ArrayList<>(discardPile));
                discardPile.clear();
            }
            players.get(idx).drawCard(deck.draw());
        }
    }

    private void advanceTurn() {
        currentPlayerIndex = Math.floorMod(currentPlayerIndex + direction, players.size());
    }

    private int nextPlayer() {
        return Math.floorMod(currentPlayerIndex + direction, players.size());
    }
}
