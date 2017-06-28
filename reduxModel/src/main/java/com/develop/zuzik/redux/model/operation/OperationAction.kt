package com.develop.zuzik.redux.model.operation

import com.develop.zuzik.redux.core.Action

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
sealed class OperationAction<Data, Progress> : Action {

	class Wait<Data, Progress> : OperationAction<Data, Progress>()
	class SetProgress<Data, Progress>(val data: Data, val progress: Progress) : OperationAction<Data, Progress>()
	class SetSuccess<Data, Progress>(val data: Data) : OperationAction<Data, Progress>()
	class SetFail<Data, Progress>(val data: Data, val error: Throwable) : OperationAction<Data, Progress>()
	class Cancel<Data, Progress>(val data: Data) : OperationAction<Data, Progress>()
}