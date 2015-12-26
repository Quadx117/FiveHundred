package fiveHundred;

import gameCore.time.GameTime;
import gameCore.time.TimeSpan;

public class FPS_Counter {

	private int frameRate;
	private int frameCounter;
	private int updateRate;
	private int updateCounter;
	private TimeSpan elapsedTime;
	private Game game;

	public FPS_Counter(Game game) {
		this.game = game;
		frameRate = 0;
		updateRate = 0;
		frameCounter = 0;
		updateCounter = 0;
		elapsedTime = new TimeSpan(TimeSpan.ZERO);
	}

	public void update(GameTime gameTime) {
		updateCounter++;
		try {
			elapsedTime.add(gameTime.getElapsedGameTime());
			if (elapsedTime.getTicks() > TimeSpan.fromSeconds(1).getTicks()) {
				elapsedTime.subtract(TimeSpan.fromSeconds(1));
				frameRate = frameCounter;
				updateRate = updateCounter;
				frameCounter = 0;
				updateCounter = 0;
				game.getFrame().setTitle(game.getTitle() + "  |  " + updateRate + " ups, " + frameRate + " fps");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void draw(GameTime gameTime) {
		frameCounter++;

	}
}
