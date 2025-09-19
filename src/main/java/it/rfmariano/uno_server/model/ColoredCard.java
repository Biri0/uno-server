package it.rfmariano.uno_server.model;

public sealed interface ColoredCard extends Card permits NumberCard, ActionCard {
    CardColor color();
}
