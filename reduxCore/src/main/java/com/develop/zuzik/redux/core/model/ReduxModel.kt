package com.develop.zuzik.redux.core.model

import android.util.Log
import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.store.Middleware
import com.develop.zuzik.redux.core.store.Reducer
import com.develop.zuzik.redux.core.store.Store
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * User: zuzik
 * Date: 4/15/17
 */
abstract class ReduxModel<State>(
		private val defaultState: State,
		private val modelScheduler: Scheduler) : Redux.Model<State> {

	override val state: BehaviorSubject<State> = BehaviorSubject.createDefault(defaultState)
	override val error: PublishSubject<Throwable> = PublishSubject.create()

	private var actions: List<Observable<Action>> = listOf()
	private var reducers: List<Reducer<State>> = listOf()
	private var middlewares: List<Middleware<State>> = listOf()

	private var disposable: Disposable? = null

	init {
		middlewares += Middleware<State> { state, action ->
			state
					.doOnNext {
						(action as? ErrorAction)
								?.error
								?.let { handleError(it) }
					}
		}
	}

	override fun init() {
		disposable = Store(defaultState, actions, reducers, middlewares)
				.bind(modelScheduler)
				.doOnError(this::handleError)
				.retry()
				.subscribe { state.onNext(it) }
	}

	override fun release() {
		disposable?.dispose()
	}

	override fun <Property> property(propertySelector: (State) -> Property): Observable<Property> =
			state
					.map(propertySelector)
					.distinctUntilChanged()

	override fun <Property> versionProperty(versionPropertySelector: (State) -> Version<Property>): Observable<Property> =
			state
					.map(versionPropertySelector)
					.distinct { it.version }
					.map { it.data }

	protected fun addAction(action: Observable<Action>) {
		actions += action
	}

	protected fun addReducer(reducer: Reducer<State>) {
		reducers += reducer
	}

	protected fun addMiddleware(middleware: Middleware<State>) {
		middlewares += middleware
	}

	private fun handleError(error: Throwable) {
		Log.d(javaClass.simpleName, error.toString())
		this.error.onNext(error)
	}
}