package gameCore.components;

import java.util.ArrayList;

public class GameComponentCollection extends ArrayList<IGameComponent> {

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	// TODO: Events
	// <summary>
	// / Event that is triggered when a <see cref="GameComponent"/> is added
	// to this <see cref="GameComponentCollection"/>.
	// </summary>
	// public event EventHandler<GameComponentCollectionEventArgs>
	// ComponentAdded;

	// <summary>
	// Event that is triggered when a <see cref="GameComponent"/> is removed
	// from this <see cref="GameComponentCollection"/>.
	// </summary>
	// public event EventHandler<GameComponentCollectionEventArgs>
	// ComponentRemoved;

	// <summary>
	// Removes every <see cref="GameComponent"/> from this <see
	// cref="GameComponentCollection"/>.
	// Triggers <see cref="OnComponentRemoved"/> once for each <see
	// cref="GameComponent"/> removed.
	// </summary>
	@Override
	public void clear() {
		for (int i = 0; i < super.size(); ++i) {
			// TODO: Events
			// this.OnComponentRemoved(new
			// GameComponentCollectionEventArgs(base[i]));
		}
		super.clear();
	}

	@Override
	public void add(int index, IGameComponent item) throws IllegalArgumentException {
		if (super.indexOf(item) != -1) {
			throw new IllegalArgumentException("Cannot Add Same Component Multiple Times");
		}
		super.add(index, item);
		if (item != null) {
			// TODO: Events
			// this.OnComponentAdded(new
			// GameComponentCollectionEventArgs(item));
		}
	}

	// TODO: Events
	/*
	 * private void onComponentAdded(GameComponentCollectionEventArgs eventArgs)
	 * {
	 * if (this.ComponentAdded != null)
	 * {
	 * this.ComponentAdded(this, eventArgs);
	 * }
	 * }
	 * // TODO: Events
	 * private void OnComponentRemoved(GameComponentCollectionEventArgs
	 * eventArgs)
	 * {
	 * if (this.ComponentRemoved != null)
	 * {
	 * this.ComponentRemoved(this, eventArgs);
	 * }
	 * }
	 */
	@Override
	public IGameComponent remove(int index) {
		IGameComponent gameComponent = super.remove(index);
		if (gameComponent != null) {
			// TODO: Events
			// this.onComponentRemoved(new
			// GameComponentCollectionEventArgs(gameComponent));
		}
		return gameComponent;
	}

	@Override
	public IGameComponent set(int index, IGameComponent item) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}
