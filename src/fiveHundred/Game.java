package fiveHundred;

import fiveHundred.cards.Card;
import fiveHundred.cards.Deck;
import fiveHundred.cards.Hand;
import fiveHundred.entity.FiveHundredAIPlayer;
import fiveHundred.entity.FiveHundredPlayer;
import fiveHundred.entity.GameTable;
import fiveHundred.entity.Player;
import fiveHundred.rules.Bid;
import fiveHundred.rules.FiveHundredRules;
import gameCore.GameCore;
import gameCore.input.Keyboard;
import gameCore.math.Vector2i;
import gameCore.time.GameTime;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/** This class contains the main methods of our game */
public class Game extends GameCore
{
	/** The enumeration for the different game states. */
	private enum GameState
	{
		DEALING_CARDS, BIDDING, PLAYING, SCORING
	}

	/** The current screen state. */
	GameState currentGameState;

	/** Default serial version UID. */
	private static final long serialVersionUID = 1L;

	/** A menu bar */
	private JMenuBar mb;

	/** The table on which the game is played. */
	private GameTable gameTable;

	/** The deck of cards used to play this game. */
	private Deck deck;

	/**
	 * The Hand object holding the cards in the widow.
	 */
	private Hand widow;

	/**
	 * The list holding all our players. The human controlled player needs
	 * always needs to be at index 0, otherwise part of the code will break.
	 */
	private List<Player> playersList;

	/** Keyboard states used to determine single key presses. */
	private Keyboard currentKeyboardState;

	/** A random number generator. */
	private Random random;

	/**
	 * The index of the player who is dealing the cards. The first one to bid
	 * will be the next player in clockwise order.
	 */
	private int dealerIndex;

	/**
	 * The index of the first player to bid or play. The first one to bid
	 * is the player next to the dealer in clockwise order.
	 */
	private int currentPlayer, firstCardPlayed = -1;

	/**
	 * The number of bids we received for one round. When this value equals the
	 * number of players, this means that every player called their bid so we
	 * can play. Passing is considered a valid bid.
	 */
	private int bidCount;

	/**
	 * The index of the highest bidding player. It is the starting player for
	 * this round.
	 */
	private int highestBidder;

	/**
	 * The current highest bid used to compare with the next player's bid.
	 */
	private Bid highestBid;

	/**
	 * The total number of cards that can be played in one round. Used to verify
	 * if every player has played all of their cards.
	 */
	private int totalNumPlayableCards;

	/**
	 * The number of cards currently played in one round. Used to verify if
	 * every player has played one of their cards.
	 */
	private int numCardsPlayed;

	/*
	 * The font used to render UI elements (score, player name, etc).
	 */
	private Font gameFont;

	/*
	 * An array containing the position at which each player's text should be rendered.
	 */
	private Vector2i[] playersTextPosition = { new Vector2i(), new Vector2i(), new Vector2i() };

	// TODO: Should calculate this from font
	private int lineHeight = 20;

	/** A FPS counter */
	private FPS_Counter fpsCounter;

	public Game()
	{
		super(800, 520, "Le 500");

		gameFont = new Font("Verdana", Font.PLAIN, 16);

		widow = new Hand();
		playersList = new ArrayList<>();
		playersList.add(new FiveHundredPlayer("Eric"));
		playersList.add(new FiveHundredAIPlayer("Left"));
		playersList.add(new FiveHundredAIPlayer("Right"));
		gameTable = new GameTable();
		random = new Random();
		mb = new JMenuBar();

		// TODO : Take care of the Keyboard input and detect single key presses
		currentKeyboardState = new Keyboard();
		addKeyListener(currentKeyboardState);

		createMenuBar();
		initBiddingComponents();
		initNextRoundButton();

		fpsCounter = new FPS_Counter(this);

		// Randomly select the first person to be the dealer
		dealerIndex = random.nextInt(playersList.size());

		deck = new Deck(Deck.CINQ_CENT, false);
		totalNumPlayableCards = playersList.size() * 10;

		// TODO: Used for AI players until they can bid by themselves
		playersList.get(1).setBid(Bid._6_PIQUES);
		playersList.get(2).setBid(Bid._6_SANS);

		initialize();
	}

	@Override
	protected void initialize()
	{
		super.initialize();

		currentGameState = GameState.DEALING_CARDS;

		// Index 0 = Human, 1 = Left, 2 = Right
		playersTextPosition[0].set((int) (super.getScreenWidth() / 3.5), super.getScreenHeight() - 161);
		playersTextPosition[1].set(50, (super.getScreenHeight() - 376) / 2 - 25);
		playersTextPosition[2].set(super.getScreenWidth() - 130, (super.getScreenHeight() - 376) / 2 - 25);
	}

	public void update(GameTime gameTime)
	{
		// TODO : Detect single key presses
		// Read the current state of the Keyboard and MouseButtons and store it
		// currentKeyboardState.update();

		// TODO : Delete when game is done.
		fpsCounter.update(gameTime);

		// Update the human player since he has mouse and keyboard controls.
		playersList.get(0).update(gameTime);

		switch (currentGameState)
		{
			case DEALING_CARDS:
				bidCount = 0;
				numCardsPlayed = 0;
				highestBid = Bid.PASS;
				deck.shuffle();
				// Deal cards to the players and the widow
				dealCards();
				// Set the initial positions of each player's cards for rendering
				setPositions();
				// Set the first player to bid.
				currentPlayer = (dealerIndex + 1) % playersList.size();
				playersList.get(currentPlayer).setYourTurn(true);
				bidDropMenu.setVisible(true);
				OK.setVisible(true);
				currentGameState = GameState.BIDDING;
				break;
			case BIDDING:
				updateBidding();
				break;
			case PLAYING:
				// Only set the cards position in the hand once.
				if (numCardsPlayed == 0)
				{
					setPositions();
				}
				playCards();
				checkRules();
				break;
			case SCORING:
				// Wait for the table to be cleared
				if (gameTable.getCardsOnTable().isEmpty())
				{
					// Wait until player presses the button
					nextRound.setVisible(true);
				}
				break;
		}

		gameTable.update(gameTime);

	}

	private void updateBidding()
	{
		Bid currentBid = null;
		// Poll the current player to see if he called a bid and store it.
		currentBid = playersList.get(currentPlayer).getBid();
		// } else {
		// playersList.get(i).update(this);

		// If a bid was called, increment the bid count and set the next player
		// to be the current player, that is the one to play.
		if (currentBid != null)
		{
			++bidCount;
			if (currentBid.ordinal() > highestBid.ordinal())
			{
				highestBid = currentBid;
				highestBidder = currentPlayer;
			}
			currentPlayer = ((currentPlayer + 1) % playersList.size());
			playersList.get(currentPlayer).setYourTurn(true);
		}

		// If all three player called their bid, start playing.
		if (bidCount == 3)
		{
			currentPlayer = highestBidder;
			currentGameState = GameState.PLAYING;
		}
	}

	/// <summary>
    /// Calculate the value of a FiveHundred card.
    /// </summary>
    /// <param name="card">The card to calculate the value for.</param>
    /// <returns>The card's value. All card values are equal to their face number.
    /// Queen = 12, King = 13 and Ace = 14. Jack = 11 when there is no trump.
    /// Otherwise, the left bower (Jack of the same suit as the trump) is 16 and the
    /// right bower (Jack of the same color as the trumo) is 15.  The Joker is always
    /// the highest card, so we give him a value of 20</returns>
    /// <remarks>An ace's value will be 1. Game logic will treat it as 11 where
    /// appropriate.</remarks>
    public int getCardValue(Card card)
    {
        switch(card.getValue())
        {
            case Card.JACK:
            	if (highestBid.hasTrump())
            	{
            		if (card.getSuit() == highestBid.getLeftBower())
            			return 16;
            		else if (card.getSuit() == highestBid.getRightBower())
            			return 15;
            		else
            			return 11;
            			
            	}
            	else
            	{
            		return 11;
            	}
            default:
                return card.getValue();
		}
    }

    public int getCardSuit(Card card)
    {
    	if (highestBid.hasTrump() &&
    		card.getValue() == Card.JACK &&
    		card.getSuit() == highestBid.getRightBower())
    	{
    		return highestBid.getLeftBower();
    	}
    	else if (highestBid.hasTrump() &&
        		card.getSuit() == Card.JOKER)
        {
        	return highestBid.getLeftBower();
        }
    	else
    	{
	        switch (card.getSuit())
	        {
	            case Card.CLUBS:
	            case Card.DIAMONDS:
	            case Card.SPADES:
	            case Card.HEARTS:
	            case Card.JOKER:
	                return card.getSuit();
	            default:
	                throw new IllegalArgumentException("Ambigous card suit");
	        }
    	}
    }

    /**
     * Helper method used by the SortHand method in CardPacket.
     * 
     * <p>
     * This method compares to card based on their suit first and then their value
     * and returns whether or not the first card comes before the second card.
     * 
     * @param card1
     *        First card to compare.
     * @param card2
     *        Second card to compare
     * @return {@code true} if the first card should be before the second one, {@code false} otherwise
     */
    private boolean cardComparator(Card card1, Card card2)
    {
        return (getCardSuit(card1) < getCardSuit(card2) ||
                (getCardSuit(card1) == getCardSuit(card2) && getCardValue(card1) < getCardValue(card2)));
    }

	private void playCards()
	{
		if (numCardsPlayed == totalNumPlayableCards)
		{
			currentGameState = GameState.SCORING;
			return;
		}
		Card card = null;

		// Poll the current player to see if he played a card and store it.
		if (gameTable.getCardsOnTable().size() < 3)
		{
			card = playersList.get(currentPlayer).playCard(this);
		}

		// If a card was played, add it to the table and set the next player's
		// playACard to true so he can play.
		if (card != null)
		{
			if (firstCardPlayed == -1)
				firstCardPlayed = currentPlayer;
			++numCardsPlayed;
			gameTable.getCardsOnTable().put(currentPlayer, card);
			currentPlayer = ((currentPlayer + 1) % playersList.size());
			playersList.get(currentPlayer).setYourTurn(true);
		}
	}

	private void checkRules()
	{
		if (gameTable.getCardsOnTable().size() != 3 || firstCardPlayed == -1)
			return;

		int trickWinner = FiveHundredRules.trickWinner(gameTable.getCardsOnTable(), firstCardPlayed, this);
		playersList.get(trickWinner).addTrick();
		gameTable.trickWinner = trickWinner;
		currentPlayer = trickWinner;
		playersList.get(currentPlayer).setYourTurn(true);
		firstCardPlayed = -1;
		// gameTable.moveTrick(trickWinner, gameTime);
	}

	protected void draw(GameTime gameTime)
	{
		gameTable.draw(spriteBatch);
		for (int i = 0; i < playersList.size(); ++i)
		{
			playersList.get(i).draw(spriteBatch);
		}
		fpsCounter.draw(gameTime);
	}

	protected void drawText(GameTime gameTime)
	{
		// TODO : Define fonts and colors
		g.setColor(Color.WHITE);
		g.setFont(gameFont);

		// Display players name, score, number of tricks won and bids.
		// If we are Bidding, display all bids, else display only the highest
		// bid.
		for (int i = 0; i < playersList.size(); ++i)
		{
			g.drawString(playersList.get(i).getName() + " : " + playersList.get(i).getScore(),
					playersTextPosition[i].getX(), playersTextPosition[i].getY());
			g.drawString("Tricks won: " + playersList.get(i).getTricksWon(), playersTextPosition[i].getX(),
					playersTextPosition[i].getY() + lineHeight);
			if (currentGameState.equals(GameState.BIDDING) && playersList.get(i).hasBid())
			{
				g.drawString(playersList.get(i).getBid().getName(), playersTextPosition[i].getX(),
						playersTextPosition[i].getY() - lineHeight);
			}
			else if (currentGameState.equals(GameState.PLAYING))
			{
				g.drawString(playersList.get(highestBidder).getBid().getName(),
						playersTextPosition[highestBidder].getX(), playersTextPosition[highestBidder].getY()
								- lineHeight);
			}
		}

		// Graphics2D g2 = (Graphics2D) g;
		// g2.rotate(Math.toRadians(gameTime.getTotalGameTime().getTotalSeconds()
		// * 24), 250, 200);
		// g2.drawString("TEST ROTATION", 200, 200);
	}

	/**
	 * Deal the cards to the player and the widow according to the rules. We
	 * first deal 3 cards to each player and the widow. The 4 to each player
	 * and finally 3 to each player again.
	 */
	private void dealCards()
	{
		for (int i = 0; i < 3; ++i)
		{
			switch (i)
			{
				case 0:
					playersList.get(0).getHand().addCard(deck.dealCard());
					playersList.get(0).getHand().addCard(deck.dealCard());
					playersList.get(0).getHand().addCard(deck.dealCard());
					playersList.get(1).getHand().addCard(deck.dealCard());
					playersList.get(1).getHand().addCard(deck.dealCard());
					playersList.get(1).getHand().addCard(deck.dealCard());
					playersList.get(2).getHand().addCard(deck.dealCard());
					playersList.get(2).getHand().addCard(deck.dealCard());
					playersList.get(2).getHand().addCard(deck.dealCard());
					widow.addCard(deck.dealCard());
					widow.addCard(deck.dealCard());
					widow.addCard(deck.dealCard());
					break;
				case 1:
					playersList.get(0).getHand().addCard(deck.dealCard());
					playersList.get(0).getHand().addCard(deck.dealCard());
					playersList.get(0).getHand().addCard(deck.dealCard());
					playersList.get(0).getHand().addCard(deck.dealCard());
					playersList.get(1).getHand().addCard(deck.dealCard());
					playersList.get(1).getHand().addCard(deck.dealCard());
					playersList.get(1).getHand().addCard(deck.dealCard());
					playersList.get(1).getHand().addCard(deck.dealCard());
					playersList.get(2).getHand().addCard(deck.dealCard());
					playersList.get(2).getHand().addCard(deck.dealCard());
					playersList.get(2).getHand().addCard(deck.dealCard());
					playersList.get(2).getHand().addCard(deck.dealCard());
					break;
				case 2:
					playersList.get(0).getHand().addCard(deck.dealCard());
					playersList.get(0).getHand().addCard(deck.dealCard());
					playersList.get(0).getHand().addCard(deck.dealCard());
					playersList.get(1).getHand().addCard(deck.dealCard());
					playersList.get(1).getHand().addCard(deck.dealCard());
					playersList.get(1).getHand().addCard(deck.dealCard());
					playersList.get(2).getHand().addCard(deck.dealCard());
					playersList.get(2).getHand().addCard(deck.dealCard());
					playersList.get(2).getHand().addCard(deck.dealCard());
					break;
			}
		}
	}

	/**
	 * Set the initial X and Y position of each player's card for rendering.
	 * These positions will later be modified after each card played.
	 */
	private void setPositions()
	{
		// TODO : Clean up positions
		// Set the x and y position of cards for rendering
		for (int i = 0; i < 10; ++i)
		{
			playersList
					.get(0)
					.getHand()
					.getCard(i)
					.setY(getScreenHeight() - playersList.get(0).getHand().getCard(i).getSprite().getHeight()
							- Card.cardSpacing);
			playersList.get(1).getHand().getCard(i).setX(50);
			playersList.get(2).getHand().getCard(i).setX(getScreenWidth() - 130);
		}
		playersList.get(0).getHand().sort(this::cardComparator);
		playersList.get(0).getHand().updateXPosition(this);

		// TODO: Delete when finished debugging
		playersList.get(1).getHand().sort(this::cardComparator);
		playersList.get(2).getHand().sort(this::cardComparator);

		playersList.get(1).getHand().updateYPosition(this);
		playersList.get(2).getHand().updateYPosition(this);
	}

	private void createMenuBar()
	{

		// Define and add drop down menu to the menu bar
		JMenu partieMenu = new JMenu("Partie");
		JMenu helpMenu = new JMenu("?");
		mb.add(partieMenu);
		mb.add(helpMenu);

		// Create and add menu items to the drop down menus
		JMenuItem newGameAction = new JMenuItem("Nouvelle partie");
		newGameAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		JMenuItem optionsAction = new JMenuItem("Options");
		optionsAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		JMenuItem appearanceAction = new JMenuItem("Modifier l'apparence");
		appearanceAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
		JMenuItem exitAction = new JMenuItem("Quitter");
		partieMenu.add(newGameAction);
		partieMenu.addSeparator();
		partieMenu.add(optionsAction);
		partieMenu.add(appearanceAction);
		partieMenu.addSeparator();
		partieMenu.add(exitAction);

		JMenuItem helpAction = new JMenuItem("Afficher l'aide");
		helpAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		helpMenu.add(helpAction);

		// Add listener to each menu item
		// File Menu
		newGameAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				// Handle new game action.

			}
		});
		optionsAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				// Handle options action.

			}
		});
		appearanceAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				// Handle appearance action.

			}
		});
		exitAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				stop();
			}
		});
		// Help Menu
		helpAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{

			}
		});
		getFrame().setJMenuBar(mb);
	}

	// Button to get out of SCORING state
	private JButton nextRound = new JButton("next round");

	private void initNextRoundButton()
	{
		nextRound.setBounds(getScreenWidth() / 2 - 50, 50, 100, 22);
		getFrame().add(nextRound);
		nextRound.setVisible(false);

		nextRound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				// advance the dealer to the next player
				dealerIndex = (dealerIndex + 1) % playersList.size();
				for (int i = 0; i < playersList.size(); ++i)
				{
					// TODO : Enable this line when AI can bid by itself
					// playersList.get(i).setBid(null);
					playersList.get(i).clearTricksWon();
				}
				// TODO : Temporary code until AI can bid by itself
				playersList.get(0).setBid(null);

				currentGameState = GameState.DEALING_CARDS;
				nextRound.setVisible(false);
			}
		});
	}

	// TODO : Refactor probably in it's own class
	private Choice bidDropMenu = new Choice();
	private JButton OK = new JButton("OK");

	private void initBiddingComponents()
	{
		bidDropMenu.setBounds(getScreenWidth() / 2 - 105, 30, 100, 30);
		List<Bid> bidList = new ArrayList<Bid>(Arrays.asList(Bid.values()));
		for (int i = 0; i < 26; ++i)
		{
			bidDropMenu.add(bidList.get(i).getName());
		}
		bidDropMenu.select(0);
		getFrame().add(bidDropMenu);
		// bidDropMenu.setVisible(false);

		OK.setBounds(getScreenWidth() / 2 + 5, 30, 80, 22);
		getFrame().add(OK);

		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				int playerBidIndex = bidDropMenu.getSelectedIndex();
				playersList.get(0).setBid(Bid.values()[playerBidIndex]);
				bidDropMenu.setVisible(false);
				OK.setVisible(false);
			}
		});
	}

	public int getNumblerOfPlayers()
	{
		return playersList.size();
	}

	public GameTable getGameTable()
	{
		return gameTable;
	}

	public int getFirstCardPlayed()
	{
		return firstCardPlayed;
	}

	public Bid getHighestBid()
	{
		return highestBid;
	}
}
// TODO : Create OverflowException extends RunTimeException
// TODO : Manage appearances
// TODO : Keep getTrumpSuit in Bid or modify Suit system in Card