package com.develop.zuzik.redux.core;

import io.reactivex.Observable;

/**
 * Created by yaroslavzozulia on 9/25/17.
 */

public interface Middleware<State> {
	Observable<State> dispatch(Observable<State> state, Action action);
}
