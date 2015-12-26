package gameCore.components;

import gameCore.GameCore;
import gameCore.graphics.SpriteBatch;
import gameCore.time.GameTime;

public class DrawableGameComponent extends GameComponent implements IDrawable {

	private boolean isInitialized;
	private int drawOrder;
	private boolean isVisible = true;

	// TODO: take care of event handling
	// public event EventHandler<EventArgs> DrawOrderChanged;
	// public event EventHandler<EventArgs> VisibleChanged;

	public DrawableGameComponent(GameCore game) {
		super(game);
	}

	@Override
	public void initialize() {
		if (!isInitialized) {
			isInitialized = true;
			loadContent();
		}
	}

	protected void loadContent() {
	}

	protected void unloadContent() {
	}

	public void draw(GameTime gameTime) {
	}

	// protected void OnVisibleChanged(object sender, EventArgs args) { }
	// protected void OnDrawOrderChanged(object sender, EventArgs args) { }

	// ++++++++++ GETTERS ++++++++++ //

	// public Graphics.GraphicsDevice GraphicsDevice() { return
	// this.Game.GraphicsDevice; }
	public SpriteBatch getGraphicsDevice() {
		return this.game.getSpriteBatch();
	}

	public int getDrawOrder() {
		return drawOrder;
	}

	public boolean isVisible() {
		return isVisible;
	}

	// ++++++++++ SETTERS ++++++++++ //

	public void setDrawOrder(int value) {
		if (drawOrder != value) {
			drawOrder = value;
			/*
			 * if (DrawOrderChanged != null)
			 * DrawOrderChanged(this, null);
			 * OnDrawOrderChanged(this, null);
			 * }
			 */
		}
	}

	public void setVisible(boolean value) {
		if (isVisible != value) {
			isVisible = value;
			/*
			 * if (VisibleChanged != null)
			 * VisibleChanged(this, EventArgs.Empty);
			 * OnVisibleChanged(this, EventArgs.Empty);
			 * }
			 */
		}
	}

}
