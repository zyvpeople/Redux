package com.develop.zuzik.redux.core

import io.reactivex.Observable

/**
 * Created by yaroslavzozulia on 9/24/17.
 */
class CompositeMiddleware<State>(private val middlewares: List<Middleware<State>>) : Middleware<State> {

	override fun dispatch(state: Observable<State>, action: Action): Observable<State> =
			middlewares.fold(state, { accumulator, middleware -> middleware.dispatch(accumulator, action) })
}