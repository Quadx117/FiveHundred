package fiveHundred.entity;

import fiveHundred.cards.Card;
import gameCore.graphics.Sprite;
import gameCore.graphics.SpriteBatch;
import gameCore.graphics.SpriteBatch.BlendState;
import gameCore.graphics.SpriteSheet;
import gameCore.math.Vector2i;
import gameCore.time.GameTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameTable {

	/** The texture used for this table's mat. The default color is red. */
	private Sprite tableMat;

	/** The different available mat for our table. */
	private List<Sprite> tableMatList;

	/**
	 * The speed at which the cards will move to their destination on the table.
	 * Expressed in pixels per millisecond.
	 */
	private double speed;

	/**
	 * The Hand object holding the cards currently on the table.
	 */
	// private Hand cardsOnTable;
	private HashMap<Integer, Card> cardsOnTable;

	/**
	 * The time in milliseconds we wait before removing the cards from the table.
	 */
	private int playedCardWaitTime;

	/** The time in milliseconds since we last updated our cardsOnTable. */
	private int elapsedTime;

	// TODO : Calculate from card's size and screen size
	/**
	 * Each player's played card destination on the table. Used for the animation.
	 */
	private Vector2i[] playedCardDest = { new Vector2i(360, 207), new Vector2i(280, 101), new Vector2i(440, 101) };
	
	/**
	 * The destination of each player's tricks won. Used for the animation.
	 */
	private Vector2i[] tricksDestination = { new Vector2i(), new Vector2i(), new Vector2i() };

	/** The number of players playing the game. */
	// TODO: Get this from the constructor
	private int numberOfPlayers = 3;

	// TODO: Make private with setter
	public int trickWinner = 0;

	// TODO: Create AnimatedGameComponent class ?
	private enum AnimationType {
		PLAY_ANIMATION, TRICK_ANIMATION, NONE
	};

	private AnimationType currentAnimation = AnimationType.NONE;

	public GameTable() {
		tableMatList = new ArrayList<>();
		tableMatList.add(new Sprite(800, 520, new SpriteSheet("/images/UI/tableRed.png", 800, 520)));
		tableMatList.add(new Sprite(800, 480, new SpriteSheet("/images/UI/tableGreen.png", 800, 480)));
		// TODO : Adjust dimension for greenMat and rename Both. Also check if
		// can do an alpha map instead.
		tableMat = tableMatList.get(0);
		playedCardWaitTime = 2500;
		elapsedTime = 0;
		speed = 1.4;

		// cardsOnTable = new Hand();
		cardsOnTable = new HashMap<>(3);

		// Index 0 = Human, 1 = Left, 2 = Right
		// TODO: Create constructor with Game parameter for game.width and
		// height or push back in Game ?
		tricksDestination[0].set((800 - Sprite.club2.getWidth()) / 2, 520);
		tricksDestination[1].set(0, (520 - Sprite.club2.getHeight()) / 2);
		tricksDestination[2].set(800, (520 - Sprite.club2.getHeight()) / 2);
	}

	// TODO : Re-factor if for AnimationType and make a switch on it after ?
	public void update(GameTime gameTime) {
		if (cardsOnTable.size() == 1)
			currentAnimation = AnimationType.PLAY_ANIMATION;
		
		/*
		 * If there are cards played on the table we animate the cards to there
		 * new position.
		 */
		if (currentAnimation == AnimationType.PLAY_ANIMATION) {
			for (int i = 0; i < numberOfPlayers; ++i) {
				Card card = cardsOnTable.get(i);
				if (card == null) continue;
				if (card.getX() != playedCardDest[i].getX() || card.getY() != playedCardDest[i].getY()) {
					moveCard(i, playedCardDest[i], gameTime);
				}
			}
		}

		/*
		 * If all cards are on the table, start counting the elapsedTime and
		 * wait until it is greater than playedCardWaitTime before removing the
		 * cards from the table so the player can see the cards played by the
		 * opponents.
		 */
		// TODO: Use TimeSpan instead
		if (cardsOnTable.size() == 3) {
			// Update the elapsed time
			elapsedTime += (int) gameTime.getElapsedGameTime().getTotalMilliseconds();
			if (elapsedTime >= playedCardWaitTime) {
				currentAnimation = AnimationType.TRICK_ANIMATION;
				if (cardsOnTable.get(trickWinner).getX() != tricksDestination[trickWinner].getX()
						|| cardsOnTable.get(trickWinner).getY() != tricksDestination[trickWinner].getY()) {
					moveTrick(tricksDestination[trickWinner], gameTime);
				} else {
					currentAnimation = AnimationType.NONE;
					cardsOnTable.clear();
					elapsedTime = 0;
				}
			}
		}
	}

	/**
	 * Moves the card on the table at position index towards the destination
	 * based on elapsed gameTime.
	 * 
	 * @param index The index of the card to move on the table. 
	 * @param destination A Vector2i representing the destination of the card.
	 * @param gameTime In-game time component.
	 */
	private void moveCard(int index, Vector2i destination, GameTime gameTime) {

		// Calculate the delta separating us from our destination
		double delatX = destination.getX() - cardsOnTable.get(index).getX();
		double deltaY = destination.getY() - cardsOnTable.get(index).getY();
		double angle = Math.atan2(deltaY, delatX);

		// Calculate the new position based on the elapsedTime
		int newX = (int) (Math.cos(angle) * speed * gameTime.getElapsedGameTime().getTotalMilliseconds());
		int newY = (int) (Math.sin(angle) * speed * gameTime.getElapsedGameTime().getTotalMilliseconds());
		if (newX < 0 && cardsOnTable.get(index).getX() + newX < destination.getX() || newX > 0
				&& cardsOnTable.get(index).getX() + newX > destination.getX()) {
			cardsOnTable.get(index).setX(destination.getX());
		} else {
			cardsOnTable.get(index).setX(cardsOnTable.get(index).getX() + newX);
		}
		if (newY < 0 && cardsOnTable.get(index).getY() + newY < destination.getY() || newY > 0
				&& cardsOnTable.get(index).getY() + newY > destination.getY()) {
			cardsOnTable.get(index).setY(destination.getY());
		} else {
			cardsOnTable.get(index).setY(cardsOnTable.get(index).getY() + newY);
		}
	}

	/**
	 * Moves the all the cards on the table towards the tricks winner
	 * destination based on elapsed gameTime.
	 * 
	 * @param destination A Vector2i representing the destination of the card.
	 * @param gameTime In-game time component.
	 */
	private void moveTrick(Vector2i destination, GameTime gameTime) {
		for (int i = 0; i < cardsOnTable.size(); ++i) {
			moveCard(i, destination, gameTime);
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(tableMat, 0, 0, BlendState.OPAQUE);

		// Render the cardsOnTable if any
		if (!cardsOnTable.isEmpty()) {
			for (int i = 0; i < numberOfPlayers; ++i) {
				Card card = cardsOnTable.get(i);
				if (card == null) continue;
				spriteBatch.draw(card.getSprite(), card.getX(), card.getY(), BlendState.ALPHA_BLEND);
			}
		}
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * Return this table's object containing the cards currently on the table.
	 * 
	 * @return this table's cardsOnTable object.
	 * */
	public HashMap<Integer, Card> getCardsOnTable() {
		return cardsOnTable;
	}
}
