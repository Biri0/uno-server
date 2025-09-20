package it.rfmariano.uno_server.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Objects;

public class Game {
    private final Deck deck;
    private final Player[] players;
    private final Deque<Card> discardPile;

    private CardColor chosenColor;
    private Integer winnerIndex;
    private Card currentCard;
    private int currentPlayerIndex;
    private int direction;

    public Game(Player[] players) {
        this.deck = new Deck();
        this.discardPile = new ArrayDeque<>();
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
        return this.players[this.currentPlayerIndex];
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
            discardPile.addLast(this.currentCard);
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
                    if (players.length == 2) {
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
        int idx = Math.floorMod(playerIndex, players.length);
        for (int i = 0; i < cards; i++) {
            if (deck.isEmpty()) {
                deck.load(new ArrayList<>(discardPile));
                discardPile.clear();
            }
            players[idx].drawCard(deck.draw());
        }
    }

    private void advanceTurn() {
        currentPlayerIndex = Math.floorMod(currentPlayerIndex + direction, players.length);
    }

    private int nextPlayer() {
        return Math.floorMod(currentPlayerIndex + direction, players.length);
    }
}
