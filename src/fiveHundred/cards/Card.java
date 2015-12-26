package fiveHundred.cards;

import gameCore.graphics.Sprite;

/**
 * An object of type Card represents a playing card from a
 * standard deck, including Jokers. The card has a suit, which
 * can be spades, hearts, diamonds, clubs, or joker. A spade, heart,
 * diamond, or club has one of the 13 values: ace, 2, 3, 4, 5, 6, 7,
 * 8, 9, 10, jack, queen, or king. Note that "ace" is considered to be
 * the smallest value. A joker can also have an associated value;
 * this value can be anything and can be used to keep track of several
 * different jokers.
 */

public class Card
{
	/* Codes for the 4 suits, plus Joker. */
	public final static int CLUBS = 0;
	public final static int DIAMONDS = 1;
	public final static int SPADES = 2;
	public final static int HEARTS = 3;
	public final static int JOKER = 4;

	/*
	 * Codes for the non-numeric cards. Cards 2 through 10 have their
	 * numerical values for their codes.
	 */
	public final static int ACE_LOW = 1;
	public final static int ACE_HIGH = 14;
	public final static int JACK = 11;
	public final static int QUEEN = 12;
	public final static int KING = 13;

	/** The amount of space in pixels between two cards in the player's hand */
	public final static int cardSpacing = 30;

	/**
	 * This card's suit, one of the constants SPADES, HEARTS, DIAMONDS,
	 * CLUBS, or JOKER. The suit cannot be changed after the card is
	 * constructed.
	 */
	private final int suit;

	/**
	 * This card's value. For a normal card, this is one of the values
	 * 1 through 13, with 1 representing ACE. For a JOKER, the value
	 * can be anything. The value cannot be changed after the card
	 * is constructed.
	 */
	private final int value;

	/** This card's sprite */
	private Sprite sprite;

	/**
	 * The x position of the card on the table
	 */
	private int x;

	/**
	 * The y position of the card on the table
	 */
	private int y;

	/**
	 * Creates a Joker, with 1 as the associated value. (Note that
	 * "new Card()" is equivalent to "new Card(1,Card.JOKER)".)
	 */
	/*
	 * public Card() {
	 * suit = JOKER;
	 * value = 1;
	 * }
	 */

	/**
	 * Creates a card with a specified suit, value and sprite.
	 * 
	 * @param theValue
	 *        The value of the new card. For a regular card (non-joker),
	 *        the value must be in the range 1 through 13, with 1
	 *        representing an Ace. You can use the constants Card.ACE,
	 *        Card.JACK, Card.QUEEN, and Card.KING. For a Joker, the value
	 *        can be anything.
	 * @param theSuit
	 *        The suit of the new card. This must be one of the values
	 *        Card.SPADES, Card.HEARTS, Card.DIAMONDS, Card.CLUBS, or
	 *        Card.JOKER.
	 * @param theSprite
	 *        The Sprite of the new card. This must be one of the values
	 *        of the Sprite class : Sprite.clubAce, Sprite.heart2,
	 *        Sprite.diamondJ, Sprite.spadeK, Sprite.firsJoker,
	 *        Sprite.secondJoker, etc.
	 * @throws IllegalArgumentException
	 *         If the parameter values are not in the
	 *         permissible ranges
	 */
	public Card(int theValue, int theSuit, Sprite theSprite) throws IllegalArgumentException
	{
		if (theSuit != SPADES && theSuit != HEARTS && theSuit != DIAMONDS && theSuit != CLUBS && theSuit != JOKER)
			throw new IllegalArgumentException("Illegal playing card suit");
		if (theSuit != JOKER && (theValue < 1 || theValue > 14)) // ACE_HIGH =
																	// 14
			throw new IllegalArgumentException("Illegal playing card value");
		value = theValue;
		suit = theSuit;
		sprite = theSprite;
		x = 0;
		y = 0;
	}

	/**
	 * Returns the suit of this card.
	 * 
	 * @returns the suit, which is one of the constants Card.SPADES,
	 *          Card.HEARTS, Card.DIAMONDS, Card.CLUBS, or Card.JOKER
	 */
	public int getSuit()
	{
		return suit;
	}

	/**
	 * Returns the value of this card.
	 * 
	 * @return the value, which is one of the numbers 1 through 13, inclusive
	 *         for a regular card, and which can be any value for a Joker.
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * Returns the sprite of this card.
	 * 
	 * @return the sprite of this card.
	 */
	public Sprite getSprite()
	{
		return sprite;
	}

	/**
	 * Returns the x position of this card.
	 * 
	 * @return the x position of this card.
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Returns the y position of this card.
	 * 
	 * @return the y position of this card.
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Returns a String representation of the card's suit.
	 * 
	 * @return one of the strings "Spades", "Hearts", "Diamonds", "Clubs"
	 *         or "Joker".
	 */
	public String getSuitAsString()
	{
		switch (suit)
		{
			case SPADES:
				return "Spades";
			case HEARTS:
				return "Hearts";
			case DIAMONDS:
				return "Diamonds";
			case CLUBS:
				return "Clubs";
			default:
				return "Joker";
		}
	}

	/**
	 * Returns a String representation of the card's value.
	 * 
	 * @return for a regular card, one of the strings "Ace", "2",
	 *         "3", ..., "10", "Jack", "Queen", or "King". For a Joker, the
	 *         string is always numerical.
	 */
	public String getValueAsString()
	{
		if (suit == JOKER)
			return "" + value;
		else
		{
			switch (value)
			{
				case 1:
					return "Ace";
				case 2:
					return "2";
				case 3:
					return "3";
				case 4:
					return "4";
				case 5:
					return "5";
				case 6:
					return "6";
				case 7:
					return "7";
				case 8:
					return "8";
				case 9:
					return "9";
				case 10:
					return "10";
				case 11:
					return "Jack";
				case 12:
					return "Queen";
				default:
					return "King";
			}
		}
	}

	/**
	 * Sets the x position of a card
	 * 
	 * @param xPos
	 *        in screen pixel
	 */
	public void setX(int xPos)
	{
		x = xPos;
	}

	/**
	 * Sets the y position of a card
	 * 
	 * @param yPos
	 *        in screen pixel
	 */
	public void setY(int yPos)
	{
		y = yPos;
	}

	/**
	 * Returns a string representation of this card, including both
	 * its suit and its value (except that for a Joker with value 1,
	 * the return value is just "Joker"). Sample return values
	 * are: "Queen of Hearts", "10 of Diamonds", "Ace of Spades",
	 * "Joker", "Joker #2"
	 */
	public String toString()
	{
		if (suit == JOKER)
		{
			if (value == 1)
				return "Joker";
			else
				return "Joker #" + value;
		}
		else
			return getValueAsString() + " of " + getSuitAsString();
	}
}
