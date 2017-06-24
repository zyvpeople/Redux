package com.develop.zuzik.redux.model.permissions

/**
 * Created by yaroslavzozulia on 6/24/17.
 */
class PermissionRequestIdProvider {

	private companion object {
		val MAX_EXCLUSIVE_OPERATION_ID = 65546
	}

	private var lastProvidedOperationId: Int = -1

	fun provide(): Int {
		lastProvidedOperationId = (lastProvidedOperationId + 1) % MAX_EXCLUSIVE_OPERATION_ID
		return lastProvidedOperationId
	}
}