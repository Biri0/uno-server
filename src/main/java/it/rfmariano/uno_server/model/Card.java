package it.rfmariano.uno_server.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumberCard.class, name = "number"),
        @JsonSubTypes.Type(value = ActionCard.class, name = "action"),
        @JsonSubTypes.Type(value = WildCard.class, name = "wild")
})
public sealed interface Card permits ColoredCard, WildCard {
    boolean isPlayableOver(Card topCard, CardColor chosenColor);
}
