package it.rfmariano.uno_server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import it.rfmariano.uno_server.model.ActionCard;
import it.rfmariano.uno_server.model.ActionType;
import it.rfmariano.uno_server.model.Card;
import it.rfmariano.uno_server.model.CardColor;
import it.rfmariano.uno_server.model.Deck;
import it.rfmariano.uno_server.model.NumberCard;
import it.rfmariano.uno_server.model.WildCard;
import it.rfmariano.uno_server.model.WildType;

@SpringBootTest
class UnoServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void deckHas108Cards() {
        Deck deck = new Deck();
        assertEquals(108, deck.size());
    }

    @Test
    void deckComposition() {
        Deck deck = new Deck();

        int colors = CardColor.values().length;
        int actions = ActionType.values().length;

        int[][] numberCounts = new int[colors][10];
        int[][] actionCounts = new int[colors][actions];
        int[] wildCounts = new int[WildType.values().length];

        Card c;
        while ((c = deck.draw()) != null) {
            if (c instanceof NumberCard nc) {
                numberCounts[nc.color().ordinal()][nc.value()]++;
            } else if (c instanceof ActionCard ac) {
                actionCounts[ac.color().ordinal()][ac.action().ordinal()]++;
            } else if (c instanceof WildCard wc) {
                wildCounts[wc.type().ordinal()]++;
            } else {
                fail("Unknown card type: " + c);
            }
        }

        assertEquals(0, deck.size());
        assertTrue(deck.isEmpty());

        for (CardColor color : CardColor.values()) {
            int idx = color.ordinal();
            assertEquals(1, numberCounts[idx][0], "Zero count for " + color);
            for (int n = 1; n <= 9; n++) {
                assertEquals(2, numberCounts[idx][n], "Count " + n + " for " + color);
            }
        }
        for (CardColor color : CardColor.values()) {
            int idx = color.ordinal();
            for (ActionType a : ActionType.values()) {
                assertEquals(2, actionCounts[idx][a.ordinal()], "Action " + a + " for " + color);
            }
        }
        assertEquals(4, wildCounts[WildType.WILD.ordinal()]);
        assertEquals(4, wildCounts[WildType.WILD_DRAW_FOUR.ordinal()]);
    }
}
