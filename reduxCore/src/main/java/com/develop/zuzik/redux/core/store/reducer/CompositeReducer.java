package com.develop.zuzik.redux.core.store.reducer;

import com.develop.zuzik.redux.core.store.Action;
import com.develop.zuzik.redux.core.store.Reducer;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * yaroslavzozulia
 * 10/4/17.
 */

public class CompositeReducer<State> implements Reducer<State> {

	private final List<Reducer<State>> reducers;

	public CompositeReducer(@NotNull List<Reducer<State>> reducers) {
		this.reducers = reducers;
	}

	@NotNull
	@Override
	public State reduce(@NotNull State state, @NotNull Action action) {
		State accumulator = state;
		for (Reducer<State> reducer : reducers) {
			accumulator = reducer.reduce(accumulator, action);
		}
		return accumulator;
	}
}