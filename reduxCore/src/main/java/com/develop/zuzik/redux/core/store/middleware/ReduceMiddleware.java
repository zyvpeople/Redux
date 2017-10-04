package com.develop.zuzik.redux.core.store.middleware;

import com.develop.zuzik.redux.core.store.Action;
import com.develop.zuzik.redux.core.store.Middleware;
import com.develop.zuzik.redux.core.store.Reducer;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * yaroslavzozulia
 * 10/4/17.
 */

public class ReduceMiddleware<State> implements Middleware<State> {

	private final Reducer<State> reducer;

	public ReduceMiddleware(@NotNull Reducer<State> reducer) {
		this.reducer = reducer;
	}

	@NotNull
	@Override
	public Observable<State> dispatch(@NotNull Observable<State> state, @NotNull final Action action) {
		return state
				.map(new Function<State, State>() {
					@Override
					public State apply(@NonNull State state) throws Exception {
						return reducer.reduce(state, action);
					}
				});
	}
}