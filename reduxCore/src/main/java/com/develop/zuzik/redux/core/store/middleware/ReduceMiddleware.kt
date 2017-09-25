package com.develop.zuzik.redux.core.store.middleware

import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.store.Middleware
import com.develop.zuzik.redux.core.store.Reducer
import io.reactivex.Observable

/**
 * Created by yaroslavzozulia on 9/24/17.
 */
class ReduceMiddleware<State>(private val reducer: Reducer<State>) : Middleware<State> {

	override fun dispatch(state: Observable<State>, action: Action): Observable<State> =
			state.map { reducer.reduce(it, action) }
}