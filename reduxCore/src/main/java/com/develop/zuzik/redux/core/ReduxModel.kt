package com.develop.zuzik.redux.core

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * User: zuzik
 * Date: 4/15/17
 */
abstract class ReduxModel<State>(private val defaultState: State) : Redux.Model<State> {

	override val state: BehaviorSubject<State> = BehaviorSubject.createDefault(defaultState)
	override val error: PublishSubject<Throwable> = PublishSubject.create()

	private var actions: List<Observable<Action>> = listOf()
	private var reducers: List<Reducer<State>> = listOf()
	private val compositeInterceptor = CompositeActionInterceptor()

	private var disposable: Disposable? = null

	init {
		compositeInterceptor.addInterceptor {
			(it as? ErrorAction)
					?.error
					?.let(this::handleError)
		}
	}

	override fun init() {
		disposable = ReduxStore(defaultState, decorate(actions), reducers)
				.bind()
				.observeOn(AndroidSchedulers.mainThread())
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

	protected fun addInterceptor(interceptor: Function1<Action, Unit>) {
		compositeInterceptor.addInterceptor(interceptor)
	}

	private fun decorate(actions: List<Observable<Action>>): List<Observable<Action>> =
			actions
					.map {
						it
								.observeOn(AndroidSchedulers.mainThread())
								.doOnNext {
									compositeInterceptor.invoke(it)
								}
					}

	private fun handleError(error: Throwable) {
		Log.d(javaClass.simpleName, error.toString())
		this.error.onNext(error)
	}
}