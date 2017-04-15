package com.develop.zuzik.redux.core

import io.reactivex.Observable

/**
 * User: zuzik
 * Date: 4/15/17
 */
class ReduxStore<State>(private val defaultState: State,
						private val actionObservables: List<Observable<Action>>,
						reducers: List<Reducer<State>>) {

	private val reducer = CompositeReducer(reducers)

	fun bind(): Observable<State> = Observable
			.merge(actionObservables)
			.scan(defaultState) { oldState, action -> reducer.reduce(oldState, action) }

}