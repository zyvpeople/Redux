package com.develop.zuzik.redux.model.operation

/**
 * Created by yaroslavzozulia on 6/27/17.
 */
sealed class OperationState<Data, Progress> {
	class Waiting<Data, Progress> : OperationState<Data, Progress>()
	class Progress<Data, Progress>(val data: Data, val progress: Progress) : OperationState<Data, Progress>()
	class Success<Data, Progress>(val data: Data) : OperationState<Data, Progress>()
	class Fail<Data, Progress>(val data: Data, val error: Throwable) : OperationState<Data, Progress>()
	class Canceled<Data, Progress>(val data: Data) : OperationState<Data, Progress>()
}