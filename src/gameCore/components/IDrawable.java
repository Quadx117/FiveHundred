package gameCore.components;

import gameCore.time.GameTime;

/**
 * 
 * @author Eric Perron (inspired by XNA framework from Microsoft)
 * 
 */
public interface IDrawable {

	// TODO: take care of the event handling
	// event EventHandler<EventArgs> DrawOrderChanged;
	// event EventHandler<EventArgs> VisibleChanged;

	void draw(GameTime gameTime);

	int getDrawOrder();

	boolean isVisible();
}
