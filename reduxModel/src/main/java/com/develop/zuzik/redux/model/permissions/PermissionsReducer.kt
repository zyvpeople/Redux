package com.develop.zuzik.redux.model.permissions

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * User: zuzik
 * Date: 5/1/17
 */
internal class PermissionsReducer : Reducer<PermissionsState> {

	override fun reduce(oldState: PermissionsState, action: Action): PermissionsState =
			(action as? PermissionsAction)?.let { reduce(oldState, it) } ?: oldState

	private fun reduce(oldState: PermissionsState, action: PermissionsAction): PermissionsState =
			when (action) {
				is PermissionsAction.AddRequest ->
					oldState.copy(requests = oldState.requests.plus(action.request.id to action.request))
				is PermissionsAction.RemoveRequest ->
					oldState.copy(requests = oldState.requests.minus(action.requestId))
				is PermissionsAction.SetCurrentRequest ->
					oldState.copy(currentRequest = action.request)
				is PermissionsAction.ClearCurrentRequest ->
					oldState.copy(
							requests = oldState.requests.minus(action.requestId),
							currentRequest = oldState
									.currentRequest
									?.let {
										if (it.id == action.requestId)
											null
										else
											it
									})
			}
}