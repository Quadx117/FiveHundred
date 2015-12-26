package fiveHundred.rules;

import java.util.HashMap;

import fiveHundred.cards.Card;

public class FiveHundredRules
{
	// TODO: Set this from Game in the constructor or delete it and pass as
	// argument in trickWinner.
	private static int numberOfPlayers = 3;

	/**
	 * Private constructor since this class only contains static methods.
	 */
	private FiveHundredRules()
	{}

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

	public static int trickWinner(HashMap<Integer, Card> cardsOnTable, int firstCardPlayed)
	{
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
			if ((cardsOnTable.get((firstCardPlayed + offset) % numberOfPlayers).getSuit() == Card.JOKER &&
					cardsOnTable.get((firstCardPlayed + offset) % numberOfPlayers).getValue() > cardsOnTable.get(
							trickWinner).getValue())
					|| (cardsOnTable.get((firstCardPlayed + offset) % numberOfPlayers).getSuit() == cardsOnTable.get(
							firstCardPlayed).getSuit()
					&& cardsOnTable.get((firstCardPlayed + offset) % numberOfPlayers).getValue() > cardsOnTable.get(
							trickWinner).getValue()))
			{
				trickWinner = (firstCardPlayed + offset) % numberOfPlayers;
			}
		}
		/*
		 * if (cardsOnTable.get((firstCardPlayed + 1) % numberOfPlayers).getSuit() == Card.JOKER
		 * && cardsOnTable.get((firstCardPlayed + 1) % numberOfPlayers).getValue() >
		 * cardsOnTable.get(trickWinner)
		 * .getValue()
		 * || cardsOnTable.get((firstCardPlayed + 1) % numberOfPlayers).getSuit() ==
		 * cardsOnTable.get(
		 * firstCardPlayed).getSuit()
		 * && cardsOnTable.get((firstCardPlayed + 1) % numberOfPlayers).getValue() >
		 * cardsOnTable.get(trickWinner)
		 * .getValue()) {
		 * trickWinner = (firstCardPlayed + 1) % numberOfPlayers;
		 * }
		 * if (cardsOnTable.get((firstCardPlayed + 2) % numberOfPlayers).getSuit() == Card.JOKER
		 * && cardsOnTable.get((firstCardPlayed + 2) % numberOfPlayers).getValue() >
		 * cardsOnTable.get(trickWinner)
		 * .getValue()
		 * || cardsOnTable.get((firstCardPlayed + 2) % numberOfPlayers).getSuit() ==
		 * cardsOnTable.get(
		 * firstCardPlayed).getSuit()
		 * && cardsOnTable.get((firstCardPlayed + 2) % numberOfPlayers).getValue() >
		 * cardsOnTable.get(trickWinner)
		 * .getValue()) {
		 * trickWinner = (firstCardPlayed + 2) % numberOfPlayers;
		 * }
		 */
		return trickWinner;
	}
}
