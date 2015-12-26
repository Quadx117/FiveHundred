package fiveHundred.entity;

import fiveHundred.Game;
import fiveHundred.cards.Card;
import gameCore.graphics.SpriteBatch;
import gameCore.graphics.SpriteBatch.BlendState;
import gameCore.time.GameTime;

import java.util.ArrayList;

public class FiveHundredAIPlayer extends Player {

	public FiveHundredAIPlayer(String name) {
		super(name);
		yourTurn = false;
	}

	public void update(GameTime gameTime) {
		// Card card = null;
		// if (yourTurn && hand.getCardCount() > 0) {
		// card = playCard(game);
		// }
		// return card;
	}

	public Card playCard(Game game) {
		Card c = null;
		if (yourTurn && hand.getCardCount() > 0) {
			yourTurn = false;
			int cardToPlay = getValidCard(game);
			c = hand.getCard(cardToPlay);
			hand.removeCard(cardToPlay);
			hand.updateYPosition(game);
		}
		return c;
	}

	// TODO : Take care of the Joker in both situations (hasTrump and not)
	// TODO : Test to see if works as expected.
	private int getValidCard(Game game) {
		int cardToPlayIndex;
		int lowestOverallCard = -1;
		
		// Since the cards are sorted in the player's hand, they will be in
		// ascending order in these ArrayList.
		// TODO : Is ArrayList the best choice of DataStructure
		ArrayList<Integer> sameSuitCards = new ArrayList<Integer>();
		ArrayList<Integer> trumpSuitCards = new ArrayList<Integer>();
		
		// If AI is the first to play
		if (game.getGameTable().getCardsOnTable().isEmpty()) {
			// TODO : Logic to find which card to play
			cardToPlayIndex = 0;
		}
		// If there are cards already played, check to see which card the AI should play.
		else {
			Card firstCardPlayed = game.getGameTable().getCardsOnTable().get(game.getFirstCardPlayed());
			for (int i = 0; i < hand.getCardCount(); ++i) {
				if (hand.getCard(i).getSuit() == firstCardPlayed.getSuit()) {
					sameSuitCards.add(i);
				}
				else if (hand.getCard(i).getSuit() == game.getHighestBid().getTrumpSuit()) {
					trumpSuitCards.add(i);
				}
				else {
					if (lowestOverallCard == -1) {
						lowestOverallCard = i;
					}
					else if (hand.getCard(i).getValue() < hand.getCard(lowestOverallCard).getValue()) {
						lowestOverallCard = i;
					}
				}
			}

			// Select which card to play
			if (!sameSuitCards.isEmpty()) {
				if (hand.getCard(sameSuitCards.get(sameSuitCards.size() - 1)).getValue() > firstCardPlayed.getValue()) {
					cardToPlayIndex = sameSuitCards.get(sameSuitCards.size() - 1);
				}
				else {
					cardToPlayIndex = sameSuitCards.get(0);
				}
			}
			else {
				if (!trumpSuitCards.isEmpty()) {
					// TODO : Check if trumpCard will win, otherwise play lowestCard
					// else if (hand.getCard(trumpSuitCards.get(trumpSuitCards.size()-1)).getValue() > firstCardPlayed.getValue()) {
					cardToPlayIndex = trumpSuitCards.get(trumpSuitCards.size() - 1);
				}
				else {
					cardToPlayIndex = lowestOverallCard;
				}
			}
		}
		
		return cardToPlayIndex;
	}
	
	public void draw(SpriteBatch spriteBatch) {
		for (int i = 0; i < hand.getCardCount(); ++i) {
			// Render cards in hand
			//spriteBatch.draw(Sprite.cardBackRed, hand.getCard(i).getX(), hand.getCard(i).getY(), BlendState.ALPHA_BLEND);
			spriteBatch.draw(hand.getCard(i).getSprite(), hand.getCard(i).getX(), hand.getCard(i).getY(), BlendState.ALPHA_BLEND);
		}
	}

}
