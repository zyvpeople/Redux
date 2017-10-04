package com.develop.zuzik.redux.core.store.middleware;

import com.develop.zuzik.redux.core.store.Action;
import com.develop.zuzik.redux.core.store.Middleware;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;

/**
 * yaroslavzozulia
 * 10/4/17.
 */

public class CompositeMiddleware<State> implements Middleware<State> {

	private final List<Middleware<State>> middlewares;

	public CompositeMiddleware(@NotNull List<Middleware<State>> middlewares) {
		this.middlewares = middlewares;
	}

	@NotNull
	@Override
	public Observable<State> dispatch(@NotNull Observable<State> state, @NotNull Action action) {
		Observable<State> accumulator = state;
		for (Middleware<State> middleware : middlewares) {
			accumulator = middleware.dispatch(accumulator, action);
		}
		return accumulator;
	}
}