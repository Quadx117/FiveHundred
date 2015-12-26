package gameCore.components;

import gameCore.GameCore;
import gameCore.time.GameTime;

public class GameComponent implements IGameComponent, IUpdateable, Comparable<GameComponent> {

	protected GameCore game;
	private boolean isEnabled = true;
	private int updateOrder;

	// TODO: take care of event handling
	// public event EventHandler<EventArgs> EnabledChanged;
	// public event EventHandler<EventArgs> UpdateOrderChanged;

	public GameComponent(GameCore game) {
		this.game = game;
	}

	public void initialize() {
	}

	public void update(GameTime gameTime) {
	}

	// protected void onUpdateOrderChanged(object sender, EventArgs args) { }
	// protected void onEnabledChanged(object sender, EventArgs args) { }

	public int compareTo(GameComponent other) {
		return other.getUpdateOrder() - this.updateOrder;
	}

	// ++++++++++ GETTERS ++++++++++ //

	public GameCore getGame() {
		return this.game;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public int getUpdateOrder() {
		return updateOrder;
	}

	// ++++++++++ SETTERS ++++++++++ //

	public void setGame(GameCore game) {
	}

	public void setEnabled(boolean value) {
		if (isEnabled != value) {
			isEnabled = value;
			/*
			 * if (this.EnabledChanged != null)
			 * this.EnabledChanged(this, EventArgs.Empty);
			 * OnEnabledChanged(this, null);
			 * }
			 */
		}
	}

	public void setUpdateOrder(int value) {
		if (updateOrder != value) {
			updateOrder = value;
			/*
			 * if (this.UpdateOrderChanged != null)
			 * this.UpdateOrderChanged(this, EventArgs.Empty);
			 * OnUpdateOrderChanged(this, null);
			 * }
			 */
		}
	}

}
