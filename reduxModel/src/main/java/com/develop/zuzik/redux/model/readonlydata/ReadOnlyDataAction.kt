package com.develop.zuzik.redux.model.readonlydata

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.ErrorAction

/**
 * User: zuzik
 * Date: 4/15/17
 */
internal sealed class ReadOnlyDataAction<Data> : Action {
	class BeginLoading<Data> : ReadOnlyDataAction<Data>()
	class Load<Data>(val data: Data) : ReadOnlyDataAction<Data>()
	class HandleError<Data>(override val error: Throwable) : ReadOnlyDataAction<Data>(), ErrorAction
}