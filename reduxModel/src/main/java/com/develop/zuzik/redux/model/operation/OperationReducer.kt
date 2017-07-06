package com.develop.zuzik.redux.model.operation

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
class OperationReducer<Input, Output, Progress> : Reducer<OperationState<Input, Output, Progress>> {

	override fun reduce(oldState: OperationState<Input, Output, Progress>, action: Action): OperationState<Input, Output, Progress> =
			(action as? OperationAction<Input, Output, Progress>)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: OperationState<Input, Output, Progress>, action: OperationAction<Input, Output, Progress>): OperationState<Input, Output, Progress> =
			when (action) {
				is OperationAction.Wait -> OperationState.Waiting()
				is OperationAction.SetProgress -> OperationState.Progress(action.input, action.progress)
				is OperationAction.SetSuccess -> OperationState.Success(action.output)
				is OperationAction.SetFail -> OperationState.Fail(action.input, action.error)
				is OperationAction.Cancel -> OperationState.Canceled(action.input)
			}
}