package com.develop.zuzik.redux.model.permissions

import com.develop.zuzik.redux.core.Action

/**
 * User: zuzik
 * Date: 5/1/17
 */
internal sealed class PermissionsAction : Action {
	class AddRequest(val request: PermissionRequest) : PermissionsAction()
	class RemoveRequest(val requestId: Int) : PermissionsAction()
	class SetCurrentRequest(val request: PermissionRequest) : PermissionsAction()
	class ClearCurrentRequest(val requestId: Int) : PermissionsAction()
}