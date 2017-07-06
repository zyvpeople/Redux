package com.develop.zuzik.redux.model.operation

import com.develop.zuzik.redux.core.Redux
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
interface Operation {

	interface Model<Input, Output, Progress> : Redux.Model<OperationState<Input, Output, Progress>> {
		val execute: Observer<Input>
		val reset: Observer<Unit>

		val success: Observable<Output>
		val fail: Observable<Pair<Input, Throwable>>
		val canceled: Observable<Input>
	}

	interface Presenter<Input, Output, Progress> : Redux.Presenter<View<Input, Output, Progress>>

	interface View<Input, Output, Progress> : Redux.View {
		val displayProgress: Observer<Pair<Input, Progress>>
		val hideProgress: Observer<Unit>
		val displaySuccess: Observer<Output>
		val hideSuccess: Observer<Unit>
		val displayError: Observer<Pair<Input, Throwable>>
		val hideError: Observer<Unit>
		val displayCanceled: Observer<Input>
		val hideCanceled: Observer<Unit>

		val displaySuccessNotification: Observer<Output>
		val displayErrorNotification: Observer<Pair<Input, Throwable>>
		val displayCanceledNotification: Observer<Input>

		val onExecute: Observable<Input>
		val onReset: Observable<Unit>
	}
}