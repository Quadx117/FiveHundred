package gameCore.components;

import gameCore.time.GameTime;

/**
 * 
 * @author Eric Perron (inspired by XNA framework from Microsoft)
 * 
 */
public interface IUpdateable {
	
	// TODO: take care of the event handling
	// event EventHandler<EventArgs> EnabledChanged;
	// event EventHandler<EventArgs> UpdateOrderChanged;

	void update(GameTime gameTime);

	boolean isEnabled();

	int getUpdateOrder();
}
