package com.develop.zuzik.redux.model.permissions

/**
 * User: zuzik
 * Date: 5/1/17
 */
data class GrantedPermission(val permission: Permission,
							 val granted: Boolean)