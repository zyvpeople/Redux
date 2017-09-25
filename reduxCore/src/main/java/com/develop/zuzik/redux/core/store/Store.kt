package com.develop.zuzik.redux.core.store

import com.develop.zuzik.redux.core.store.reducer.CompositeReducer
import com.develop.zuzik.redux.core.store.middleware.CompositeMiddleware
import com.develop.zuzik.redux.core.store.middleware.ReduceMiddleware
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject

/**
 * User: zuzik
 * Date: 4/15/17
 */
class Store<State>(private val defaultState: State,
				   private val actionObservables: List<Observable<Action>>,
				   reducers: List<Reducer<State>>,
				   middlewares: List<Middleware<State>>) {

	private val middleware = CompositeMiddleware(middlewares + ReduceMiddleware(CompositeReducer(reducers)))

	fun bind(scheduler: Scheduler): Observable<State> =
			Observable
					.defer {
						val stateSubject = BehaviorSubject.createDefault(defaultState)
						Observable
								.merge(actionObservables)
								.observeOn(scheduler)
								.concatMap { middleware.dispatch(stateSubject.take(1), it) }
								.distinctUntilChanged { oldState, newState -> oldState === newState }
								.distinctUntilChanged()
								.observeOn(scheduler)
								.doOnNext { stateSubject.onNext(it) }
								.startWith(defaultState)
					}
}