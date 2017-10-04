package com.develop.zuzik.redux.core.store.reducer;

import com.develop.zuzik.redux.core.store.Action;
import com.develop.zuzik.redux.core.store.Reducer;

import org.jetbrains.annotations.NotNull;

/**
 * yaroslavzozulia
 * 10/4/17.
 */

public abstract class ActionReducer<State, A extends Action> implements Reducer<State> {

	private final Class<A> actionClass;

	@NotNull
	protected abstract State reduceAction(@NotNull State state, @NotNull A action);

	public ActionReducer(@NotNull Class<A> actionClass) {
		this.actionClass = actionClass;
	}

	@NotNull
	@Override
	public final State reduce(@NotNull State state, @NotNull Action action) {
		return actionClass.isAssignableFrom(action.getClass())
				? reduceAction(state, (A) action)
				: state;
	}
}