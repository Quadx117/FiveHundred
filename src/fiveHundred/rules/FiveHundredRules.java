package fiveHundred.rules;

import java.util.HashMap;

import fiveHundred.Game;
import fiveHundred.cards.Card;

public class FiveHundredRules
{
	/**
	 * Private constructor since this class only contains static methods.
	 */
	private FiveHundredRules() {}

	/**
	 * This method returns the index of the player who played the highest card
	 * during this trick.
	 * 
	 * @param cardsOnTable
	 *        The HashMap containing the cards currently played on the table.
	 * @param firstCardPlayed
	 *        An index into the map of cards that represents the first card played on the table.
	 * @return The index of the player who played the highest card during this
	 *         trick.
	 */
	public static int trickWinner(HashMap<Integer, Card> cardsOnTable, int firstCardPlayed, Game game)
	{
		int numberOfPlayers = game.getNumblerOfPlayers();
		
		// We start by assuming that the player who played first is the
		// trickWinner. We compare him to the next player to his left and
		// determine which is the winner between them. We than repeat this
		// process between the current trickWinner and the other player.
		int trickWinner = firstCardPlayed;

		// If the card is a Joker and it is higher than the other card (if we
		// are using 2 Jokers) or if the card is the same suit as the other and
		// it is higher.
		// TODO: Add rules for trump (maybe adjust card values for trump higher)
		for (int offset = 1; offset < numberOfPlayers; ++offset)
		{
			if ((game.getCardSuit(cardsOnTable.get((firstCardPlayed + offset) % numberOfPlayers)) == Card.JOKER &&
					game.getCardValue(cardsOnTable.get((firstCardPlayed + offset) % numberOfPlayers)) >
					game.getCardValue(cardsOnTable.get(trickWinner))) ||
				(game.getCardSuit(cardsOnTable.get((firstCardPlayed + offset) % numberOfPlayers)) ==
						game.getCardSuit(cardsOnTable.get(firstCardPlayed)) &&
				 game.getCardValue(cardsOnTable.get((firstCardPlayed + offset) % numberOfPlayers)) >
					game.getCardValue(cardsOnTable.get(trickWinner))))
			{
				trickWinner = (firstCardPlayed + offset) % numberOfPlayers;
			}
		}

		return trickWinner;
	}
}
