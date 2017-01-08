package fiveHundred.rules;

import fiveHundred.cards.Card;

/**
 * This enumeration contains the different available bids for the game of
 * Five-Hundred based on the Avondale Schedule. They are created in their
 * natural ordering so that their ordinal number is their rank relative to the
 * others. In other words, the lowest bid has the lowest ordinal and score
 * value.
 * 
 * <p>
 * This enables us to compare two bids naturally by their ordinal number to verify which one is
 * bigger, that is, has the highest score value.
 * 
 * @author Eric Perron
 * 
 */
public enum Bid
{
	PASS("Pass", Trump.NO_TRUMP, -1, -1, 0),
	_6_PIQUES("6 Piques", Trump.SPADES, Card.SPADES, Card.CLUBS, 40),
	_6_TREFLES("6 Trèfles", Trump.CLUBS, Card.CLUBS, Card.SPADES, 60),
	_6_CARREAU("6 Carreau", Trump.DIAMONDS, Card.DIAMONDS, Card.HEARTS, 80),
	_6_COEUR("6 Coeurs", Trump.HEARTS, Card.HEARTS, Card.DIAMONDS, 100),
	_6_SANS("6 Sans Atout", Trump.NO_TRUMP, -1, -1, 120),
	_7_PIQUES("7 Piques", Trump.SPADES, Card.SPADES, Card.CLUBS, 140),
	_7_TREFLES("7 Trèfles", Trump.CLUBS, Card.CLUBS, Card.SPADES, 160),
	_7_CARREAU("7 Carreau", Trump.DIAMONDS, Card.DIAMONDS, Card.HEARTS, 180),
	_7_COEUR("7 Coeurs", Trump.HEARTS, Card.HEARTS, Card.DIAMONDS, 200),
	_7_SANS("7 Sans Atout", Trump.NO_TRUMP, -1, -1, 220),
	_8_PIQUES("8 Piques", Trump.SPADES, Card.SPADES, Card.CLUBS, 240),
	_8_TREFLES("8 Trèfles", Trump.CLUBS, Card.CLUBS, Card.SPADES, 260),
	_8_CARREAU("8 Carreau", Trump.DIAMONDS, Card.DIAMONDS, Card.HEARTS, 280),
	_8_COEUR("8 Coeurs", Trump.HEARTS, Card.HEARTS, Card.DIAMONDS, 300),
	_8_SANS("8 Sans Atout", Trump.NO_TRUMP, -1, -1, 320),
	_9_PIQUES("9 Piques", Trump.SPADES, Card.SPADES, Card.CLUBS, 340),
	_9_TREFLES("9 Trèfles", Trump.CLUBS, Card.CLUBS, Card.SPADES, 360),
	_9_CARREAU("9 Carreau", Trump.DIAMONDS, Card.DIAMONDS, Card.HEARTS, 380),
	_9_COEUR("9 Coeurs", Trump.HEARTS, Card.HEARTS, Card.DIAMONDS, 400),
	_9_SANS("9 Sans Atout", Trump.NO_TRUMP, -1, -1, 420),
	_10_PIQUES("10 Piques", Trump.SPADES, Card.SPADES, Card.CLUBS, 440),
	_10_TREFLES("10 Trèfles", Trump.CLUBS, Card.CLUBS, Card.SPADES, 460),
	_10_CARREAU("10 Carreau", Trump.DIAMONDS, Card.DIAMONDS, Card.HEARTS, 480),
	_10_COEUR("10 Coeurs", Trump.HEARTS, Card.HEARTS, Card.DIAMONDS, 500),
	_10_SANS("10 Sans Atout", Trump.NO_TRUMP, -1, -1, 520);

	/**
	 * The enumeration of the different trumps available.
	 */
	private enum Trump
	{
		SPADES, CLUBS, DIAMONDS, HEARTS, NO_TRUMP
	}

	/**
	 * The name of the bid as it will appear to the player on the screen.
	 */
	private String name;

	/**
	 * The trump for this bid. Enables us to adjust the cards value according to the
	 * trump of the highest bid.
	 */
	private Trump trump;

	/**
	 * The scoreValue in points of the bid. This is how much points will
	 * be awarded for winning the bid.
	 */
	private int scoreValue;

	/**
	 * The suit of this bid's trump. Used to compare to a card's suit.
	 * The values here must match the values of the suits in Card.
	 */
	private int trumpSuit;

	/**
	 * If this bid has a trump, this is the Jack of the same suit.
	 */
	private int leftBower;

	/**
	 * If this bid has a trump, this is the Jack of the suit of the same color.
	 */
	private int rightBower;

	/**
	 * Initializes a new instance of the Bid object with the specified values.
	 * 
	 * @param name
	 *        The name of this bid.
	 * @param trump
	 *        The trump of this bid.
	 * @param scoreValue
	 *        The scoreValue of this bid.
	 */
	private Bid(String name, Trump trump, int leftBower, int rightBower, int scoreValue)
	{
		this.name = name;
		this.trump = trump;
		this.scoreValue = scoreValue;
		this.leftBower = leftBower;
		this.rightBower = rightBower;

		trumpSuit = -1;
		if (trump.name().equals(Trump.CLUBS.name()))
		{
			trumpSuit = Card.CLUBS;
		}
		else if (trump.name().equals(Trump.DIAMONDS.name()))
		{
			trumpSuit = Card.DIAMONDS;
		}
		else if (trump.name().equals(Trump.HEARTS.name()))
		{
			trumpSuit = Card.HEARTS;
		}
		else if (trump.name().equals(Trump.SPADES.name()))
		{
			trumpSuit = Card.SPADES;
		}
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * Return the name of this Bid.
	 * 
	 * @return the name of this Bid.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Return the trump of this Bid.
	 * 
	 * @return the trump of this Bid.
	 */
	public Trump getTrump()
	{
		return trump;
	}

	/**
	 * Return the suit of the left bower for this Bid.
	 * 
	 * @return the trump of this Bid.
	 */
	public int getLeftBower()
	{
		return leftBower;
	}

	/**
	 * Return the suit of the right bower for this Bid.
	 * 
	 * @return the trump of this Bid.
	 */
	public int getRightBower()
	{
		return rightBower;
	}

	/**
	 * Return an int value that represents the suit of this trump. A value of
	 * -1 means NO_TRUMP.
	 * 
	 * @return The suit of this trump.
	 */
	public int getTrumpSuit()
	{
		return trumpSuit;
	}

	/**
	 * Return {@code true} if this bid has a trump.
	 * 
	 * @return {@code true} if this bid has a trump.
	 */
	public boolean hasTrump()
	{
		return this.trump != Trump.NO_TRUMP;
	}

	/**
	 * Return the scoreValue of this Bid.
	 * 
	 * @return the scoreValue of this Bid.
	 */
	public int getScoreValue()
	{
		return scoreValue;
	}

}
