package com.develop.zuzik.redux.core.model

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * User: zuzik
 * Date: 4/15/17
 */
abstract class ReduxPresenter<in V : Redux.View> : Redux.Presenter<V> {

	private val compositeDisposable = CompositeDisposable()

	protected fun intent(disposable: Disposable) {
		compositeDisposable.add(disposable)
	}

	override final fun onStop() {
		compositeDisposable.clear()
	}
}