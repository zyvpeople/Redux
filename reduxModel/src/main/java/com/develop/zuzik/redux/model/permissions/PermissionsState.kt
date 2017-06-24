package com.develop.zuzik.redux.model.permissions

/**
 * User: zuzik
 * Date: 5/1/17
 */
//TODO: also need to store operation's tag for debug purposes
data class PermissionsState(val requests: Map<Int, PermissionRequest>,
							val currentRequest: PermissionRequest?)