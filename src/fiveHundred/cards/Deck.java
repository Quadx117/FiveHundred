package fiveHundred.cards;

import gameCore.graphics.Sprite;

import java.util.Arrays;
import java.util.List;

/**
 * An object of type Deck represents a deck of playing cards. The deck
 * is a regular poker deck that contains 52 regular cards and that can
 * also optionally include two Jokers.
 */
public class Deck {

	/**
	 * An array of cards. A 54-card deck contains two Jokers in addition to the
	 * 52 cards of a regular poker deck.
	 */
	private Card[] deck;

	/**
	 * Keeps track of the number of cards that have been dealt from
	 * the deck so far.
	 */
	private int cardsUsed;

	/**
	 * The list of sprite used in the constructor to associate a card to a
	 * sprite
	 */
	List<Sprite> spriteList = Arrays.asList(Sprite.clubAce, Sprite.club2, Sprite.club3, Sprite.club4, Sprite.club5,
			Sprite.club6, Sprite.club7, Sprite.club8, Sprite.club9, Sprite.club10, Sprite.clubJ, Sprite.clubQ,
			Sprite.clubK, Sprite.diamondAce, Sprite.diamond2, Sprite.diamond3, Sprite.diamond4, Sprite.diamond5,
			Sprite.diamond6, Sprite.diamond7, Sprite.diamond8, Sprite.diamond9, Sprite.diamond10, Sprite.diamondJ,
			Sprite.diamondQ, Sprite.diamondK, Sprite.spadeAce, Sprite.spade2, Sprite.spade3, Sprite.spade4,
			Sprite.spade5, Sprite.spade6, Sprite.spade7, Sprite.spade8, Sprite.spade9, Sprite.spade10, Sprite.spadeJ,
			Sprite.spadeQ, Sprite.spadeK, Sprite.heartAce, Sprite.heart2, Sprite.heart3, Sprite.heart4, Sprite.heart5,
			Sprite.heart6, Sprite.heart7, Sprite.heart8, Sprite.heart9, Sprite.heart10, Sprite.heartJ, Sprite.heartQ,
			Sprite.heartK, Sprite.firstJoker, Sprite.secondJoker);

	/**
	 * Codes for the types of deck we could initialize
	 */
	public final static int NORMAL = 0;
	public final static int CINQ_CENT = 1;

	/**
	 * Constructs a regular 52-card poker deck. Initially, the cards
	 * are in a sorted order. The shuffle() method can be called to
	 * randomize the order. (Note that "new Deck()" is equivalent
	 * to "new Deck(false)".)
	 */
	public Deck() {
		this(NORMAL, false); // Just call the other constructor in this class.
	}

	// TODO : Adjust comments, create more types of decks create more
	// constructors
	/**
	 * Constructs a poker deck of playing cards. The deck contains
	 * the usual 52 cards and can optionally contain two Jokers
	 * in addition, for a total of 54 cards. Initially the cards
	 * are in a sorted order. The shuffle() method can be called to
	 * randomize the order.
	 * 
	 * @param typeOfDeck
	 *            if NORMAL equals 52 cards, if CINQ_CENT equals 33 cards
	 *            including one Joker. includeJokers is ignored.
	 * @param includeJokers
	 *            if true, two Jokers are included in the deck; if false,
	 *            there are no Jokers in the deck.
	 */
	public Deck(int typeOfDeck, boolean includeJokers) {
		if (includeJokers) deck = new Card[54];
		else if (typeOfDeck == NORMAL) deck = new Card[52];
		else if (typeOfDeck == CINQ_CENT) deck = new Card[33];

		int cardCount = 0; // How many cards have been created so far.

		if (typeOfDeck == NORMAL) {
			for (int suit = 0; suit <= 3; suit++) {
				for (int value = 1; value <= 13; value++) {
					deck[cardCount] = new Card(value, suit, spriteList.get(value - 1 + suit * 13));
					cardCount++;
				}
			}
		} else if (typeOfDeck == CINQ_CENT) {
			for (int suit = 0; suit <= 3; suit++) {
				for (int value = 7; value <= 13; value++) {
					deck[cardCount] = new Card(value, suit, spriteList.get(value - 1 + suit * 13));
					cardCount++;
				}
			}
			deck[28] = new Card(Card.ACE_HIGH, Card.CLUBS, Sprite.clubAce);
			deck[29] = new Card(Card.ACE_HIGH, Card.DIAMONDS, Sprite.diamondAce);
			deck[30] = new Card(Card.ACE_HIGH, Card.HEARTS, Sprite.heartAce);
			deck[31] = new Card(Card.ACE_HIGH, Card.SPADES, Sprite.spadeAce);
			deck[32] = new Card(20, Card.JOKER, Sprite.firstJoker);
		}

		if (includeJokers) {
			deck[52] = new Card(1, Card.JOKER, Sprite.firstJoker);
			deck[53] = new Card(2, Card.JOKER, Sprite.secondJoker);
		}
		cardsUsed = 0;
	}

	// TODO : Adjust comments (Put card back into the deck ???)
	/**
	 * Put all the used cards back into the deck (if any), and
	 * shuffle the deck into a random order.
	 */
	public void shuffle() {
		for (int i = deck.length - 1; i > 0; i--) {
			int rand = (int) (Math.random() * (i + 1));
			Card temp = deck[i];
			deck[i] = deck[rand];
			deck[rand] = temp;
		}
		cardsUsed = 0;
	}

	/**
	 * As cards are dealt from the deck, the number of cards left
	 * decreases. This function returns the number of cards that
	 * are still left in the deck. The return value would be
	 * 52 or 54 (depending on whether the deck includes Jokers)
	 * when the deck is first created or after the deck has been
	 * shuffled. It decreases by 1 each time the dealCard() method
	 * is called.
	 */
	public int cardsLeft() {
		return deck.length - cardsUsed;
	}

	/**
	 * Removes the next card from the deck and return it. It is illegal
	 * to call this method if there are no more cards in the deck. You can
	 * check the number of cards remaining by calling the cardsLeft() function.
	 * 
	 * @return the card which is removed from the deck.
	 * @throws IllegalStateException
	 *             if there are no cards left in the deck
	 */
	public Card dealCard() {
		if (cardsUsed == deck.length) throw new IllegalStateException("No cards are left in the deck.");
		cardsUsed++;
		return deck[cardsUsed - 1];
		// Programming note: Cards are not literally removed from the array
		// that represents the deck. We just keep track of how many cards
		// have been used.
	}

	/**
	 * Test whether the deck contains Jokers.
	 * 
	 * @return true, if this is a 54-card deck containing two jokers, or false
	 *         if
	 *         this is a 52 card deck that contains no jokers.
	 */
	public boolean hasJokers() {
		return (deck.length == 54);
	}
}
