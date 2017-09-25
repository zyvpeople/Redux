package com.develop.zuzik.redux.model.application

import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.model.ErrorAction

/**
 * Created by yaroslavzozulia on 7/24/17.
 */
internal sealed class ApplicationAction<Data> : Action {
	class Initialize<Data>(val data: Data) : ApplicationAction<Data>()
	class HandleError<Data>(override val error: Throwable) : ApplicationAction<Data>(), ErrorAction
}