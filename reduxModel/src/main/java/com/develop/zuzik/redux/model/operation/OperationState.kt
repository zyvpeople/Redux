package com.develop.zuzik.redux.model.operation

/**
 * Created by yaroslavzozulia on 6/27/17.
 */
sealed class OperationState<Input, Output, Progress> {
	class Waiting<Input, Output, Progress> : OperationState<Input, Output, Progress>()
	class Progress<Input, Output, Progress>(val input: Input, val progress: Progress) : OperationState<Input, Output, Progress>()
	class Success<Input, Output, Progress>(val output: Output) : OperationState<Input, Output, Progress>()
	class Fail<Input, Output, Progress>(val input: Input, val error: Throwable) : OperationState<Input, Output, Progress>()
	class Canceled<Input, Output, Progress>(val input: Input) : OperationState<Input, Output, Progress>()
}