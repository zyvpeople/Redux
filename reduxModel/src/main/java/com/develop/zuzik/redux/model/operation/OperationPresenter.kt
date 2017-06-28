package com.develop.zuzik.redux.model.operation

import com.develop.zuzik.redux.core.ReduxPresenter
import com.develop.zuzik.redux.core.extension.UnitInstance
import com.develop.zuzik.redux.core.extension.asConsumer

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
class OperationPresenter<Data, Progress>(private val model: Operation.Model<Data, Progress>) :
		Operation.Presenter<Data, Progress>,
		ReduxPresenter<Operation.View<Data, Progress>>() {

	override fun onStart(view: Operation.View<Data, Progress>) {
		intent(model
				.state
				.subscribe { renderView(it, view)() })

		intent(view
				.onExecute
				.subscribe(model.execute.asConsumer()))
		intent(view
				.onReset
				.subscribe(model.reset.asConsumer()))
	}

	private fun renderView(state: OperationState<Data, Progress>, view: Operation.View<Data, Progress>): () -> Unit = when (state) {
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
				view.displayProgress.onNext(state.data to state.progress)
				view.hideSuccess.onNext(UnitInstance.INSTANCE)
				view.hideError.onNext(UnitInstance.INSTANCE)
				view.hideCanceled.onNext(UnitInstance.INSTANCE)
			}
		}
		is OperationState.Success -> {
			{
				view.hideProgress.onNext(UnitInstance.INSTANCE)
				view.displaySuccess.onNext(state.data)
				view.hideError.onNext(UnitInstance.INSTANCE)
				view.hideCanceled.onNext(UnitInstance.INSTANCE)
				view.displaySuccessNotification.onNext(state.data)
			}
		}
		is OperationState.Fail -> {
			{
				view.hideProgress.onNext(UnitInstance.INSTANCE)
				view.hideSuccess.onNext(UnitInstance.INSTANCE)
				view.displayError.onNext(state.data to state.error)
				view.hideCanceled.onNext(UnitInstance.INSTANCE)
				view.displayErrorNotification.onNext(state.data to state.error)
			}
		}
		is OperationState.Canceled -> {
			{
				view.hideProgress.onNext(UnitInstance.INSTANCE)
				view.hideSuccess.onNext(UnitInstance.INSTANCE)
				view.hideError.onNext(UnitInstance.INSTANCE)
				view.displayCanceled.onNext(state.data)
				view.displayCanceledNotification.onNext(state.data)
			}
		}
	}
}