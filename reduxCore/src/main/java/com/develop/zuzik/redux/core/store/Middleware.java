package com.develop.zuzik.redux.core.store;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;

/**
 * Created by yaroslavzozulia on 9/25/17.
 */

public interface Middleware<State> {
	@NotNull
	Observable<State> dispatch(@NotNull Observable<State> state, @NotNull Action action);
}
