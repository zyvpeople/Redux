package com.develop.zuzik.redux.model.application

/**
 * Created by yaroslavzozulia on 7/24/17.
 */
sealed class ApplicationState<Data> {
	class Initializing<Data> : ApplicationState<Data>()
	class Initialized<Data>(val data: Data) : ApplicationState<Data>()
	class Error<Data>(val error: Throwable) : ApplicationState<Data>()
}