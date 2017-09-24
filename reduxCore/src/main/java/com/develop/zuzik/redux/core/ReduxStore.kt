package com.develop.zuzik.redux.core

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * User: zuzik
 * Date: 4/15/17
 */
class ReduxStore<State>(private val defaultState: State,
						private val actionObservables: List<Observable<Action>>,
						reducers: List<Reducer<State>>,
						middlewares: List<Middleware<State>>) {

	private val middleware = CompositeMiddleware(middlewares + ReduceMiddleware(CompositeReducer(reducers)))

	fun bind(): Observable<State> =
			Observable
					.defer {
						val stateSubject = BehaviorSubject.createDefault(defaultState)
						Observable
								.merge(actionObservables)
								.concatMap { middleware.dispatch(stateSubject.take(1), it) }
								.distinctUntilChanged { oldState, newState -> oldState === newState }
								.distinctUntilChanged()
								.doOnNext { stateSubject.onNext(it) }
								.startWith(defaultState)
					}
}