package com.develop.zuzik.redux.model.application

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * Created by yaroslavzozulia on 7/24/17.
 */
internal class ApplicationReducer<Data> : Reducer<ApplicationState<Data>> {

	override fun reduce(oldState: ApplicationState<Data>, action: Action): ApplicationState<Data> =
			(action as? ApplicationAction<Data>)?.let(this::reduce) ?: oldState

	private fun reduce(action: ApplicationAction<Data>): ApplicationState<Data> =
			when (action) {
				is ApplicationAction.Initialize -> ApplicationState.Initialized(action.data)
				is ApplicationAction.HandleError -> ApplicationState.Error(action.error)
			}
}