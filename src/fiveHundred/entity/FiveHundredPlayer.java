package fiveHundred.entity;

import fiveHundred.Game;
import fiveHundred.cards.Card;
import gameCore.graphics.Sprite;
import gameCore.graphics.SpriteBatch;
import gameCore.graphics.SpriteBatch.BlendState;
import gameCore.input.Mouse;
import gameCore.time.GameTime;

public class FiveHundredPlayer extends Player
{
	/**
	 * The index of the selected card, that is, the one that is currently
	 * hovered by the mouse.
	 */
	private int selectedCardIndex;

	/** Mouse buttons states used to determine single clicks */
	public int previousButtonState, currentButtonState;

	public FiveHundredPlayer(String name)
	{
		super(name);
		previousButtonState = currentButtonState = -1;
	}

	public void update(GameTime gameTime)
	{
		// Store the previous mouse button states.
		previousButtonState = currentButtonState;

		// Read the current mouse button states.
		currentButtonState = Mouse.getButton();

		updateSelectedCard();
		// if (yourTurn && selectedCardIndex >= 0 && currentButtonState !=
		// previousButtonState) {
		// card = playCard(game);
		// }
		// return card;
	}

	/**
	 * Check if a card is currently selected (currently hovered by the mouse)
	 * and set selectedCardIndex accordingly.
	 * 
	 * <p>
	 * The possible values for selectedCardIndex are 0 to hand.getCardCount() - 1 if a card is
	 * selected or -1 if no card is selected.
	 */
	private void updateSelectedCard()
	{
		selectedCardIndex = -1;
		for (int i = 0; i < hand.getCardCount(); i++)
		{
			// Selection area is bigger for the rightmost card in the player's
			// hand since it is completely visible.
			if (i == hand.getCardCount() - 1)
			{
				if (Mouse.getX() > hand.getCard(i).getX() &&
					Mouse.getX() < hand.getCard(i).getX() + hand.getCard(i).getSprite().getWidth() &&
					Mouse.getY() > hand.getCard(i).getY() &&
					Mouse.getY() < hand.getCard(i).getY() + hand.getCard(i).getSprite().getHeight())
				{
					selectedCardIndex = i;
					break;
				}
			}
			// For all the other cards, the selection area is smaller since
			// they are partially covered by the card to their right
			else
			{
				if (Mouse.getX() > hand.getCard(i).getX() &&
					Mouse.getX() < hand.getCard(i).getX() + Card.cardSpacing &&
					Mouse.getY() > hand.getCard(i).getY() &&
					Mouse.getY() < hand.getCard(i).getY() + hand.getCard(i).getSprite().getHeight())
				{
					selectedCardIndex = i;
					break;
				}
			}
		}
	}

	public Card playCard(Game game)
	{
		Card c = null;
		if (yourTurn && selectedCardIndex >= 0 && currentButtonState != previousButtonState)
		{
			c = hand.getCard(selectedCardIndex);
			if (!validCard(game, c))
			{
				return null;
			}
			yourTurn = false;
			hand.removeCard(selectedCardIndex);
			hand.updateXPosition(game);
		}
		return c;
	}

	private boolean validCard(Game game, Card card)
	{
		boolean cardIsValid = false;

		if (!game.getGameTable().getCardsOnTable().isEmpty())
		{
			Card firstCardPlayed = game.getGameTable().getCardsOnTable().get(game.getFirstCardPlayed());

			boolean hasSameSuitCards = false;

			for (int i = 0; i < hand.getCardCount(); ++i)
			{
				if (game.getCardSuit(hand.getCard(i)) == game.getCardSuit(firstCardPlayed))
				{
					hasSameSuitCards = true;
					break;
				}
			}

			if (!hasSameSuitCards)
			{
				cardIsValid = true;
			}
			else
			{
				if ((game.getCardSuit(card) == Card.JOKER) || (game.getCardSuit(card) == game.getCardSuit(firstCardPlayed)))
				{
					cardIsValid = true;
				}
			}
		}
		else
		{
			cardIsValid = true;
		}

		return cardIsValid;
	}

	public void draw(SpriteBatch spriteBatch)
	{
		for (int i = 0; i < hand.getCardCount(); i++)
		{
			// Render cards in hand
			spriteBatch.draw(hand.getCard(i).getSprite(), hand.getCard(i).getX(), hand.getCard(i).getY(),
					BlendState.ALPHA_BLEND);
			// Render card highlight
			if (i == selectedCardIndex)
			{
				spriteBatch.draw(Sprite.cardHighlight, hand.getCard(i).getX() - 2, hand.getCard(i).getY() - 2,
						BlendState.ALPHA_BLEND);
			}
		}
	}

}
