package com.develop.zuzik.redux.model.operation

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
class OperationReducer<Data, Progress> : Reducer<OperationState<Data, Progress>> {

	override fun reduce(oldState: OperationState<Data, Progress>, action: Action): OperationState<Data, Progress> =
			(action as? OperationAction<Data, Progress>)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: OperationState<Data, Progress>, action: OperationAction<Data, Progress>): OperationState<Data, Progress> =
			when (action) {
				is OperationAction.Wait -> OperationState.Waiting()
				is OperationAction.SetProgress -> OperationState.Progress(action.data, action.progress)
				is OperationAction.SetSuccess -> OperationState.Success(action.data)
				is OperationAction.SetFail -> OperationState.Fail(action.data, action.error)
				is OperationAction.Cancel -> OperationState.Canceled(action.data)
			}
}