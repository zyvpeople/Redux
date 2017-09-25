package com.develop.zuzik.redux.model.operation

import com.develop.zuzik.redux.core.store.Action

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
sealed class OperationAction<Input, Output, Progress> : Action {

	class Wait<Input, Output, Progress> : OperationAction<Input, Output, Progress>()
	class SetProgress<Input, Output, Progress>(val input: Input, val progress: Progress) : OperationAction<Input, Output, Progress>()
	class SetSuccess<Input, Output, Progress>(val output: Output) : OperationAction<Input, Output, Progress>()
	class SetFail<Input, Output, Progress>(val input: Input, val error: Throwable) : OperationAction<Input, Output, Progress>()
	class Cancel<Input, Output, Progress>(val input: Input) : OperationAction<Input, Output, Progress>()
}