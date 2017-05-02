package com.develop.zuzik.redux.model.permissions

/**
 * User: zuzik
 * Date: 4/30/17
 */
data class PermissionResponse(val operationId: Int,
							  val grantedPermissions: List<GrantedPermission>)