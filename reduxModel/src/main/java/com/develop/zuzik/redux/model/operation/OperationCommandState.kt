package com.develop.zuzik.redux.model.operation

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
sealed class OperationCommandState<Input, Output, Progress> {
	class Progress<Input, Output, Progress>(val input: Input, val progress: Progress) : OperationCommandState<Input, Output, Progress>()
	class Success<Input, Output, Progress>(val output: Output) : OperationCommandState<Input, Output, Progress>()
	class Fail<Input, Output, Progress>(val input: Input, val error: Throwable) : OperationCommandState<Input, Output, Progress>()
}