package fiveHundred.entity;

import fiveHundred.Game;
import fiveHundred.cards.Card;
import fiveHundred.cards.Hand;
import fiveHundred.rules.Bid;
import gameCore.graphics.SpriteBatch;
import gameCore.time.GameTime;

public abstract class Player {

	/**
	 * Value indicating if it is this player's turn to play. Default value is
	 * false.
	 */
	// TODO : Not sure if i'm going to use it this way
	protected boolean yourTurn;

	/**
	 * This player's hand of cards
	 */
	protected Hand hand;

	/** This player's number of tricks won in one round */
	protected int tricksWon;

	/** This player's score */
	protected int score;

	/** This player's name */
	protected String name;

	// TODO : Not sure if i'm going to use it this way
	protected Bid bid;

	public Player(String name) {
		hand = new Hand();
		tricksWon = 0;
		score = 0;
		this.name = name;
		yourTurn = false;
		bid = null;
	}

	/**
	 * Called when the game has determined that the player's logic needs to be
	 * processed. Override this method with player-specific logic.
	 * 
	 * @param gameTime
	 *            The Game's GameTime Object.
	 */
	public abstract void update(GameTime gameTime);

	/**
	 * Called when the game determines it is time to draw the player. Override
	 * this method with player-specific rendering code.
	 * 
	 * @param spriteBatch
	 *            A reference to the spriteBatch object that will paint a frame.
	 */
	public abstract void draw(SpriteBatch spriteBatch);

	/**
	 * Plays a card and return it so it can be added to the table. Override this
	 * method with player-specific code.
	 * 
	 * <p>
	 * This method should play a card only at the appropriate time and if all
	 * the specific conditions are true. If it cannot play a card, it should
	 * return {@code null}.
	 * 
	 * @return The card played by that player or {@code null}.
	 */
	public abstract Card playCard(Game game);

	/**
	 * Adds one to this player's number of tricks won.
	 */
	public void addTrick() {
		++tricksWon;
	}

	/**
	 * Reset this player's number of tricks won to 0.
	 */
	public void clearTricksWon() {
		tricksWon = 0;
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * Return this player's hand
	 * 
	 * @return this player's hand
	 * */
	public Hand getHand() {
		return hand;
	}

	/**
	 * Return this player's number of tricks won in this round.
	 * 
	 * @return the number of tricks won.
	 */
	public int getTricksWon() {
		return tricksWon;
	}

	/**
	 * Return this player's score.
	 * 
	 * @return this player's score.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Return this player's name.
	 * 
	 * @return this player's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return this player's bid.
	 * 
	 * @return this player's bid.
	 */
	public Bid getBid() {
		return bid;
	}

	/**
	 * Return true if this player has placed a bid.
	 * 
	 * @return true if this player have placed a bid.
	 */
	public boolean hasBid() {
		return bid == null ? false : true;
	}

	/**
	 * Return true if it's this player's turn.
	 * 
	 * @return true if it's this player's turn.
	 */
	public boolean isYourTurn() {
		return yourTurn;
	}

	// ++++++++++ SETTERS ++++++++++ //

	// TODO : Check if needed after testing.
	/**
	 * Set the bid to the specified value.
	 * 
	 * @param value
	 *            the new value to be assigned to this variable.
	 */
	public void setBid(Bid value) {
		bid = value;
	}

	// TODO : Check if needed after testing.
	/**
	 * Set yourTurn to the specified value.
	 * 
	 * @param value
	 *            the new value to be assigned to this variable.
	 */
	public void setYourTurn(boolean value) {
		yourTurn = value;
	}
}
