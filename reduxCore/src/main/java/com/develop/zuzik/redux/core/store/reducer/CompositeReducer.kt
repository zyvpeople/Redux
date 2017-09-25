package com.develop.zuzik.redux.core.store.reducer

import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.store.Reducer

/**
 * User: zuzik
 * Date: 4/15/17
 */
class CompositeReducer<State>(private val reducers: List<Reducer<State>>) : Reducer<State> {
	override fun reduce(oldState: State, action: Action): State =
			reducers.fold(oldState) { state, reducer -> reducer.reduce(state, action) }
}