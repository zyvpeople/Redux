package com.develop.zuzik.redux.model.readonlydata

import com.develop.zuzik.redux.core.Redux
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * User: zuzik
 * Date: 4/15/17
 */
interface ReadOnlyData {

	interface Model<Data> : Redux.Model<ReadOnlyDataState<Data>> {
		val refresh: Observer<Unit>
	}

	interface View<in Data> : Redux.View {
		val displayProgress: Observer<in Boolean>
		val displayData: Observer<in Data>
		val displayError: Observer<in Throwable>
		val hideError: Observer<in Unit>
		val displayErrorNotification: Observer<in Throwable>

		val onRefresh: Observable<Unit>
	}

	interface Presenter<out Data> : Redux.Presenter<View<Data>>
}