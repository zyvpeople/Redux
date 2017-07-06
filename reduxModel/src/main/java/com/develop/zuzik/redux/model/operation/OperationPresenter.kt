package com.develop.zuzik.redux.model.operation

import com.develop.zuzik.redux.core.ReduxPresenter
import com.develop.zuzik.redux.core.extension.UnitInstance
import com.develop.zuzik.redux.core.extension.asConsumer

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
class OperationPresenter<Input, Output, Progress>(private val model: Operation.Model<Input, Output, Progress>) :
		Operation.Presenter<Input, Output, Progress>,
		ReduxPresenter<Operation.View<Input, Output, Progress>>() {

	override fun onStart(view: Operation.View<Input, Output, Progress>) {
		intent(model
				.state
				.subscribe { renderView(it, view)() })
		intent(model
				.success
				.subscribe(view.displaySuccessNotification.asConsumer()))
		intent(model
				.fail
				.subscribe(view.displayErrorNotification.asConsumer()))
		intent(model
				.canceled
				.subscribe(view.displayCanceledNotification.asConsumer()))

		intent(view
				.onExecute
				.subscribe(model.execute.asConsumer()))
		intent(view
				.onReset
				.subscribe(model.reset.asConsumer()))
	}

	private fun renderView(state: OperationState<Input, Output, Progress>, view: Operation.View<Input, Output, Progress>): () -> Unit = when (state) {
		is OperationState.Waiting -> {
			{
				view.hideProgress.onNext(UnitInstance.INSTANCE)
				view.hideSuccess.onNext(UnitInstance.INSTANCE)
				view.hideError.onNext(UnitInstance.INSTANCE)
				view.hideCanceled.onNext(UnitInstance.INSTANCE)
			}
		}
		is OperationState.Progress -> {
			{
				view.displayProgress.onNext(state.input to state.progress)
				view.hideSuccess.onNext(UnitInstance.INSTANCE)
				view.hideError.onNext(UnitInstance.INSTANCE)
				view.hideCanceled.onNext(UnitInstance.INSTANCE)
			}
		}
		is OperationState.Success -> {
			{
				view.hideProgress.onNext(UnitInstance.INSTANCE)
				view.displaySuccess.onNext(state.output)
				view.hideError.onNext(UnitInstance.INSTANCE)
				view.hideCanceled.onNext(UnitInstance.INSTANCE)
			}
		}
		is OperationState.Fail -> {
			{
				view.hideProgress.onNext(UnitInstance.INSTANCE)
				view.hideSuccess.onNext(UnitInstance.INSTANCE)
				view.displayError.onNext(state.input to state.error)
				view.hideCanceled.onNext(UnitInstance.INSTANCE)
			}
		}
		is OperationState.Canceled -> {
			{
				view.hideProgress.onNext(UnitInstance.INSTANCE)
				view.hideSuccess.onNext(UnitInstance.INSTANCE)
				view.hideError.onNext(UnitInstance.INSTANCE)
				view.displayCanceled.onNext(state.input)
			}
		}
	}
}