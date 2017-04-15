package com.develop.zuzik.redux.core.extension

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * User: zuzik
 * Date: 4/15/17
 */

fun <T> Consumer<T>.asObserver(): Observer<T> = object : Observer<T> {
	override fun onComplete() {
	}

	override fun onSubscribe(d: Disposable?) {
	}

	override fun onNext(t: T) {
		accept(t)
	}

	override fun onError(e: Throwable?) {
	}
}