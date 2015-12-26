package gameCore;

import gameCore.components.GameComponentCollection;
import gameCore.graphics.SpriteBatch;
import gameCore.input.Mouse;
import gameCore.time.GameTime;
import gameCore.time.Stopwatch;
import gameCore.time.TimeSpan;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

/**
 * This class contains the core of the game and shoudldn't be exposed to the
 * client. The game logic goes into Game which inherits from this class.
 * 
 * <p>
 * The mouse and it's associated listeners are handled here, but everything
 * related to the keyboard or other inputs have to be handled in the child
 * class.
 */
// TODO: Keep name or rename to Game.
// TODO: Keep name of folder or rename to framework
public abstract class GameCore extends Canvas implements Runnable {

	/** Default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * The width of the screen in pixels. The default value is 800
	 * 
	 * <p>
	 * This value can be overridden in the constructor of the child class.
	 */
	private int screenWidth;

	/**
	 * The height of the screen in pixels. The default value is 600
	 * 
	 * <p>
	 * This value can be overridden in the constructor of the child class.
	 */
	private int screenHeight;

	/**
	 * The title of the game displayed in the title bar. The default value is
	 * "Game"
	 * 
	 * <p>
	 * This value can be overridden in the constructor of the child class.
	 */
	private String title;

	/** True if the game is running */
	private boolean isRunning;

	/**
	 * Value indicating whether to use fixed time steps, {@code true} by
	 * default.
	 * 
	 * A fixed-step Game tries to call its update method on the fixed interval
	 * specified in targetElapsedTime. Setting Game.isFixedTimeStep to
	 * {@code true} causes a Game to use a fixed-step game loop. A new project
	 * uses a fixed-step game loop with a default targetElapsedTime of 1/60th of
	 * a second.
	 * 
	 * In a fixed-step game loop, Game calls update once the targetElapsedTime
	 * has elapsed. After update is called, if it is not time to call update
	 * again, Game calls Draw. After Draw is called, if it is not time to call
	 * update again, Game idles until it is time to call update.
	 * 
	 * If update takes too long to process, Game sets isRunningSlowly to
	 * {@code true} and calls update again, without calling Draw in between.
	 * When an update runs longer than the targetElapsedTime, Game responds by
	 * calling update extra times and dropping the frames associated with those
	 * updates to catch up. This ensures that update will have been called the
	 * expected number of times when the game loop catches up from a slowdown.
	 * You can check the value of isRunningSlowly in your update if you want to
	 * detect dropped frames and shorten your update processing to compensate.
	 * You can reset the elapsed times by calling resetElapsedTime.
	 */
	private boolean isFixedTimeStep = true;

	/**
	 * Value indicating whether to skip the call to draw until the next update.
	 * Default value is set to {@code false}
	 */
	private boolean suppressDraw = false;

	/**
	 * Value indicating whether the mouse cursor should be visible.
	 */
	private boolean isMouseVisible = true;

	/**
	 * Target time between calls to {@code update} when {@code isFixedTimeStep}
	 * is {@code true}.
	 * 
	 * When the game frame rate is less than targetElapsedTime, isRunningSlowly
	 * will return {@code true}.
	 * 
	 * The default value for targetElapsedTime is 1/60th of a second.
	 * 
	 * A fixed-step Game tries to call its update method on the fixed interval
	 * specified in targetElapsedTime. Setting Game.isFixedTimeStep to
	 * {@code true} causes a Game to use a fixed-step game loop. A new project
	 * uses a fixed-step game loop with a default targetElapsedTime of 1/60th of
	 * a second.
	 * 
	 * In a fixed-step game loop, Game calls update once the targetElapsedTime
	 * has elapsed. After update is called, if it is not time to call update
	 * again, Game calls Draw. After Draw is called, if it is not time to call
	 * update again, Game idles until it is time to call update.
	 * 
	 * If update takes too long to process, Game sets isRunningSlowly to
	 * {@code true} and calls update again, without calling Draw in between.
	 * When an update
	 * runs longer than the targetElapsedTime, Game responds by calling update
	 * extra times and dropping the frames associated with those updates to
	 * catch up. This ensures that update will have been called the expected
	 * number of times when the game loop catches up from a slowdown. You can
	 * check the value of isRunningSlowly in your update if you want to detect
	 * dropped frames and shorten your update processing to compensate. You can
	 * reset the elapsed times by calling resetElapsedTime.
	 * */
	private TimeSpan targetElapsedTime;

	// TODO : Complete comments
	/**
	 * Specifies the maximum length we will be updating the game before calling
	 * the {@code draw()} method. The default value is 500 milliseconds.
	 * 
	 * If something outside update is taking too much time, we only catch up to
	 * this maximum value. This makes sure we will call the {@code draw()}
	 * method at least once every time interval specified by this variable.
	 */
	private TimeSpan maxElapsedTime;

	/** The accumulated elapsed time between each tick */
	private TimeSpan accumulatedElapsedTime;

	/**
	 * Holds the number of frame we skipped while we were running too slow
	 */
	private int updateFrameLag;

	/** The thread the game is running on */
	private Thread gameThread;

	/** The object used to draw one frame in the BufferedImage */
	protected SpriteBatch spriteBatch;
	/** The object used to draw the image in the frame */
	private BufferedImage image;
	/** The frame to display the image */
	private JFrame frame;
	/** The BufferStrategy used in this game */
	private BufferStrategy buffStrat;
	/** The graphic context used to draw onto the component */
	protected Graphics g;
	/** The array of pixels to be painted on the screen */
	private int[] pixels;

	/** The object used to hold our elapsed and total game time */
	private GameTime gameTime;

	/** The timer used to measure elapsed time */
	private Stopwatch gameTimer;

	/** Our time keepers used to calculate and update gameTime */
	private long currentTicks, previousTicks;

	// TODO: Comments
	private GameComponentCollection _components;

	/**
	 * Initializes a new instance of this class with the default screenWidth,
	 * screenHeight and title, which provides basic graphics device
	 * initialization, game logic, rendering code, and a game loop.
	 */
	protected GameCore() {
		this(800, 600, "Game");
	}

	/**
	 * Initializes a new instance of this class, which provides basic graphics
	 * device initialization, game logic, rendering code, and a game loop.
	 */
	protected GameCore(final int SCREEN_WIDTH, final int SCREEN_HEIGHT, String title) {
		this.screenWidth = SCREEN_WIDTH;
		this.screenHeight = SCREEN_HEIGHT;
		this.title = title;
		isRunning = false;
		previousTicks = 0;

		_components = new GameComponentCollection();
		accumulatedElapsedTime = new TimeSpan(0);
		targetElapsedTime = new TimeSpan(TimeSpan.fromTicks(166667));
		maxElapsedTime = new TimeSpan(TimeSpan.fromMilliseconds(500));

		image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		spriteBatch = new SpriteBatch(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame = new JFrame();
		gameTimer = new Stopwatch();
		gameTime = new GameTime();

		Dimension size = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
	}

	/**
	 * frame.setVisible is here which enables us to manipulate the frame in the
	 * child class before making it visible. The child class should call this
	 * method at the end of its constructor.
	 * 
	 * <p>
	 * This makes it possible, for example, to add a menu bar in the constructor
	 * of our child class.
	 */
	protected void initialize() {
		Mouse mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);

		frame.setResizable(false);
		frame.setTitle(title);
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		// Initialize the buffer strategy
		createBufferStrategy(3);
		buffStrat = getBufferStrategy();
		g = buffStrat.getDrawGraphics();

		frame.setVisible(true);
	}

	/** Start the gameThread and gameTimer effectively starting the game */
	public synchronized void start() {
		isRunning = true;
		gameThread = new Thread(this, "Game");
		gameThread.start();
	}

	/** Stop the game thread and exit */
	public synchronized void stop() {
		isRunning = false;
		// simulate clicking on the X button on the frame to close it.
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start the game timer, begin running the game loop, and start processing
	 * events for the game. Automatically called by gameThread.start()
	 */
	public void run() {
		requestFocus();
		gameTimer = Stopwatch.startNew();
		gameLoop();
		// We call stop in case we got out of the game loop abnormally
		stop();
	}

	/** The main loop for our game */
	private void gameLoop() {
		while (isRunning) {
			tick();
			// draw();
		}
	}

	// Holds the delta between targetElapsedTime and accumulatedElapasedTime
	private TimeSpan delta = new TimeSpan(0);

	/**
	 * Updates the game's clock and calls update and draw.
	 * 
	 * This is the main update method of our game which updates the game logic
	 * and draws to the screen. If both of these operations cannot be done in
	 * less time than our targetElapsedTime, then we skip draws and only update
	 * to catch up.
	 * 
	 * In a fixed-step game, Tick calls Update only after a target time interval
	 * has elapsed.
	 * 
	 * In a variable-step game, Update is called every time Tick is called.
	 */
	private void tick() {
		do {
			// Advance our time keepers.
			currentTicks = gameTimer.getElapsedTicks();
			// TODO : Do we want to have to handle the exception or not
			try {
				accumulatedElapsedTime.add(TimeSpan.fromTicks(currentTicks - previousTicks));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			previousTicks = currentTicks;
			/*
			 * If we're in the fixed-time step mode and not enough time has
			 * elapsed to perform an update we sleep off the the remaining time
			 * to save battery life and/or release CPU time to other threads and
			 * processes.
			 */
			if (isFixedTimeStep && accumulatedElapsedTime.getTicks() < targetElapsedTime.getTicks()) {
				try {
					delta.setTimeSpan((TimeSpan.subtract(targetElapsedTime, accumulatedElapsedTime)).getTicks());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int sleepTime = (int) delta.getTotalMilliseconds();

				// NOTE: While sleep can be inaccurate in general it is
				// accurate enough for frame limiting purposes if some
				// fluctuation is an acceptable result.
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				break;
			}
		} while (true);

		// Do not allow any update to take longer than our maximum. This makes
		// sure we draw once every time interval specified by maxElapsedTime.
		if (accumulatedElapsedTime.getTicks() > maxElapsedTime.getTicks()) accumulatedElapsedTime = maxElapsedTime;

		if (isFixedTimeStep) {
			gameTime.setElapsedGameTime(targetElapsedTime);
			int updateCount = 0;

			// Perform as many full fixed length time steps as we can.
			while (accumulatedElapsedTime.getTicks() >= targetElapsedTime.getTicks()) {
				try {
					gameTime.getTotalGameTime().add(targetElapsedTime);
					accumulatedElapsedTime.subtract(targetElapsedTime);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updateCount++;
				update(gameTime);
			}

			// Every update after the first accumulates lag
			updateFrameLag += Math.max(0, updateCount - 1);

			// If we think we are running slowly, wait until the lag clears
			// before resetting it
			if (gameTime.isRunningSlowly()) {
				if (updateFrameLag == 0) gameTime.setIsRunningSlowly(false);
			} else if (updateFrameLag >= 5) {
				// If we lag more than 5 frames, start thinking we are running
				// slowly
				gameTime.setIsRunningSlowly(true);
			}

			// Every time we just do one update and one draw, then we are not
			// running slowly, so decrease the lag
			if (updateCount == 1 && updateFrameLag > 0) updateFrameLag--;

			// Draw needs to know the total elapsed time that occurred for the
			// fixed length updates.
			gameTime.setElapsedGameTime(TimeSpan.fromTicks(targetElapsedTime.getTicks() * updateCount));
		} else {
			// Perform a single variable length update.
			gameTime.setElapsedGameTime(accumulatedElapsedTime);
			try {
				gameTime.getTotalGameTime().add(accumulatedElapsedTime);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			accumulatedElapsedTime = TimeSpan.ZERO;

			update(gameTime);
		}

		// Draw unless the update suppressed it.
		if (suppressDraw) {
			suppressDraw = false;
		} else {
			beginDraw();
			draw(gameTime);
			drawImageToScreen();
			drawText(gameTime);
			endDraw();
		}
	}

	/**
	 * Starts the drawing of a frame. This method is followed by calls to draw,
	 * drawImageToScreen, drawText and EndDraw.
	 * 
	 * <p>
	 * This method clears the last frame drawn so we can start to draw the next
	 * frame. It does so by setting the array of pixels used to draw on the
	 * screen to black, making it available to receive new pixel data to be
	 * drawn.
	 */
	private void beginDraw() {
		spriteBatch.clear();
	}

	/**
	 * Called when the game has determined that game logic needs to be
	 * processed. This might include the management of the game state, the
	 * processing of user input, or the updating of simulation data. Override
	 * this method with game-specific logic.
	 * 
	 * <p>
	 * update and draw are called at different rates depending on whether
	 * isFixedTimeStep is {@code true} or {@code false}. If isFixedTimeStep is
	 * {@code false}, update and draw will be called in a continuous loop,
	 * sequentially as often as possible.. If isFixedTimeStep is {@code true},
	 * update will be called at the interval specified in targetElapsedTime,
	 * while draw will only be called if an update is not due. If draw is not
	 * called, isRunningSlowly will be set to {@code true}.
	 */
	protected abstract void update(GameTime gameTime);

	/**
	 * Called when the game determines it is time to draw a frame. Override this
	 * method with game-specific rendering code.
	 * 
	 * <p>
	 * update and draw are called at different rates depending on whether
	 * isFixedTimeStep is {@code true} or {@code false}. If isFixedTimeStep is
	 * {@code false}, update and draw will be called in a continuous loop,
	 * sequentially as often as possible.. If isFixedTimeStep is {@code true},
	 * update will be called at the interval specified in targetElapsedTime,
	 * while draw will only be called if an update is not due. If draw is not
	 * called, isRunningSlowly will be set to {@code true}.
	 */
	protected abstract void draw(GameTime gameTime);

	/** Draws the image from the buffer to the screen */
	private void drawImageToScreen() {
		buffStrat = getBufferStrategy();
		if (buffStrat == null) {
			createBufferStrategy(3);
			return;
		}

		for (int i = 0; i < pixels.length; ++i) {
			pixels[i] = spriteBatch.screenPixels[i];
		}

		g = buffStrat.getDrawGraphics();
		g.drawImage(image, 0, 0, getScreenWidth(), getScreenHeight(), null);
	}

	/**
	 * Draws text to the screen. Subclasses must override this method.
	 */
	protected abstract void drawText(GameTime gameTime);

	/**
	 * Disposes of this graphics context and releases any system resources that
	 * it is using and makes the next available buffer visible.
	 */
	private void endDraw() {
		g.dispose();
		buffStrat.show();
	}

	/**
	 * Resets the elapsed time counter.
	 * 
	 * Use this method if your game is recovering from a slow-running state, and
	 * elapsedGameTime is too large to be useful.
	 */
	public void resetElapsedTime() {
		gameTimer.reset();
		gameTimer.start();
		accumulatedElapsedTime = TimeSpan.ZERO;
		gameTime.setElapsedGameTime(TimeSpan.ZERO);
		previousTicks = 0L;
	}

	/**
	 * Prevents calls to draw until the next update.
	 * 
	 * Call this method during update to prevent any calls to draw until after
	 * the next call to update. This method can be used on small devices to
	 * conserve battery life if the display does not change as a result of
	 * update. For example, if the screen is static with no background
	 * animations, the player input can be examined during update to determine
	 * whether the player is performing any action. If no input is detected,
	 * this method allows the game to skip drawing until the next update.
	 */
	public void suppressDraw() {
		suppressDraw = true;
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * Returns the spriteBatch object used to draw a frame.
	 * 
	 * @return The spriteBatch object used to draw a frame.
	 */
	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	/** Returns the width of the game screen */
	public int getScreenWidth() {
		return screenWidth;
	}

	/** Returns the height of the game screen */
	public int getScreenHeight() {
		return screenHeight;
	}

	/** Returns the frame used by this game */
	public JFrame getFrame() {
		return frame;
	}

	/** Returns the title of this game */
	public String getTitle() {
		return title;
	}

	/** Returns the maxElapsedTime used by this game */
	public TimeSpan getMaxElapsedTime() {
		return maxElapsedTime;
	}

	/**
	 * Returns {@code true} if this game uses fixed time step.
	 * 
	 * @return {@code true} if this game uses fixed time step; {@code false}
	 *         otherwise.
	 */
	public boolean isFixedTimeStep() {
		return isFixedTimeStep;
	}

	/**
	 * Returns {@code true} if the mouse should be visible in the game.
	 * 
	 * @return {@code true} if the mouse cursor should be visible; {@code false}
	 *         otherwise.
	 */
	public boolean isMouseVisible() {
		return isMouseVisible;
	}

	/**
	 * 
	 * @return
	 */
	public GameComponentCollection getComponents() {
		return _components;
	}// TODO: Events added and removed

	// ++++++++++ SETTERS ++++++++++ //

	/** Set the maxElapsedTime to the specified value */
	public void setMaxElapsedTime(TimeSpan value) {
		if (value.getTicks() < TimeSpan.ZERO.getTicks())
			throw new IllegalArgumentException("The time must be positive.");
		if (value.getTicks() < targetElapsedTime.getTicks())
			throw new IllegalArgumentException("The time must be at least targetElapsedTime");

		maxElapsedTime = value;
	}

	/**
	 * Set isFixedTimeStep to the specified value.
	 * 
	 * @param value
	 *            the new value to be assigned to this variable.
	 */
	public void setIsFixedTimeStep(boolean value) {
		isFixedTimeStep = value;
	}

	/**
	 * Set isMouseVisible to the specified value.
	 * 
	 * @param value
	 *            the new value to be assigned to this variable.
	 */
	public void setIsMouseVisible(boolean value) {
		isMouseVisible = value;
	}
}
// TODO : Handle isMouseVisible
// TODO : Finsh comments (getters and setters, etc)