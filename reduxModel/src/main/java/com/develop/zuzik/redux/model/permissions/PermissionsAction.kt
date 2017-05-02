package com.develop.zuzik.redux.model.permissions

import com.develop.zuzik.redux.core.Action

/**
 * User: zuzik
 * Date: 5/1/17
 */
internal sealed class PermissionsAction : Action {
	class RegisterOperation(val operationId: Int) : PermissionsAction()
	class UnregisterOperation(val operationId: Int) : PermissionsAction()
}