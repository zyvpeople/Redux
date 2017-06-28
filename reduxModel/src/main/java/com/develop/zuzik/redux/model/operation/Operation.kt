package com.develop.zuzik.redux.model.operation

import com.develop.zuzik.redux.core.Redux
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
interface Operation {

	interface Model<Data, Progress> : Redux.Model<OperationState<Data, Progress>> {
		val execute: Observer<Data>
		val reset: Observer<Unit>
	}

	interface Presenter<Data, Progress> : Redux.Presenter<View<Data, Progress>>

	interface View<Data, Progress> : Redux.View {
		val displayProgress: Observer<Pair<Data, Progress>>
		val hideProgress: Observer<Unit>
		val displaySuccess: Observer<Data>
		val hideSuccess: Observer<Unit>
		val displayError: Observer<Pair<Data, Throwable>>
		val hideError: Observer<Unit>
		val displayCanceled: Observer<Data>
		val hideCanceled: Observer<Unit>

		val displaySuccessNotification: Observer<Data>
		val displayErrorNotification: Observer<Pair<Data, Throwable>>
		val displayCanceledNotification: Observer<Data>

		val onExecute: Observable<Data>
		val onReset: Observable<Unit>
	}
}