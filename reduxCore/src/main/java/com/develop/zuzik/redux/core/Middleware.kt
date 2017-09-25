package com.develop.zuzik.redux.core

import io.reactivex.Observable

/**
 * Created by yaroslavzozulia on 9/24/17.
 */
//TODO: use it like lambda
interface Middleware<State> {
	fun dispatch(state: Observable<State>, action: Action): Observable<State>
}