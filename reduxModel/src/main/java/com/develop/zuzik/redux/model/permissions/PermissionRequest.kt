package com.develop.zuzik.redux.model.permissions

/**
 * User: zuzik
 * Date: 4/30/17
 */
data class PermissionRequest(val operationId: Int,
							 val permissions: List<Permission>)