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
				is PermissionsAction.RegisterOperation -> oldState.copy(operationIds = oldState.operationIds.plus(action.operationId))
				is PermissionsAction.UnregisterOperation -> oldState.copy(operationIds = oldState.operationIds.minus(action.operationId))
			}
}