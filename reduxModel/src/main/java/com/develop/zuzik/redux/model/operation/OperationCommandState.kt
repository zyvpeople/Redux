package com.develop.zuzik.redux.model.operation

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
sealed class OperationCommandState<Data, Progress> {
	class Progress<Data, Progress>(val data: Data, val progress: Progress) : OperationCommandState<Data, Progress>()
	class Success<Data, Progress>(val data: Data) : OperationCommandState<Data, Progress>()
	class Fail<Data, Progress>(val data: Data, val error: Throwable) : OperationCommandState<Data, Progress>()
}