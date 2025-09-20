package it.rfmariano.uno_server.model;

public sealed interface Card permits ColoredCard, WildCard {
    boolean isPlayableOver(Card topCard, CardColor chosenColor);
}
