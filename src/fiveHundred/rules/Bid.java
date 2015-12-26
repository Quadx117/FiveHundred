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
	PASS("Pass", Trump.NO_TRUMP, 0), 					//
	_6_PIQUES("6 Piques", Trump.SPADES, 40), 			//
	_6_TREFLES("6 Trèfles", Trump.CLUBS, 60),			//
	_6_CARREAU("6 Carreau", Trump.DIAMONDS, 80),		//
	_6_COEUR("6 Coeurs", Trump.HEARTS, 100),			//
	_6_SANS("6 Sans Atout", Trump.NO_TRUMP, 120),		//
	_7_PIQUES("7 Piques", Trump.SPADES, 140),			//
	_7_TREFLES("7 Trèfles", Trump.CLUBS, 160),			//
	_7_CARREAU("7 Carreau", Trump.DIAMONDS, 180),		//
	_7_COEUR("7 Coeurs", Trump.HEARTS, 200),			//
	_7_SANS("7 Sans Atout", Trump.NO_TRUMP, 220),		//
	_8_PIQUES("8 Piques", Trump.SPADES, 240),			//
	_8_TREFLES("8 Trèfles", Trump.CLUBS, 260),			//
	_8_CARREAU("8 Carreau", Trump.DIAMONDS, 280),		//
	_8_COEUR("8 Coeurs", Trump.HEARTS, 300),			//
	_8_SANS("8 Sans Atout", Trump.NO_TRUMP, 320),		//
	_9_PIQUES("9 Piques", Trump.SPADES, 340),			//
	_9_TREFLES("9 Trèfles", Trump.CLUBS, 360),			//
	_9_CARREAU("9 Carreau", Trump.DIAMONDS, 380),		//
	_9_COEUR("9 Coeurs", Trump.HEARTS, 400),			//
	_9_SANS("9 Sans Atout", Trump.NO_TRUMP, 420),		//
	_10_PIQUES("10 Piques", Trump.SPADES, 440),			//
	_10_TREFLES("10 Trèfles", Trump.CLUBS, 460),		//
	_10_CARREAU("10 Carreau", Trump.DIAMONDS, 480),		//
	_10_COEUR("10 Coeurs", Trump.HEARTS, 500),			//
	_10_SANS("10 Sans Atout", Trump.NO_TRUMP, 520);

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
	 * The trump this bid. Enables us to adjust the cards value according to the
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
	 * Initializes a new instance of the Bid object with the specified values.
	 * 
	 * @param name
	 *        The name of this bid.
	 * @param trump
	 *        The trump of this bid.
	 * @param scoreValue
	 *        The scoreValue of this bid.
	 */
	private Bid(String name, Trump trump, int scoreValue)
	{
		this.name = name;
		this.trump = trump;
		this.scoreValue = scoreValue;

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
