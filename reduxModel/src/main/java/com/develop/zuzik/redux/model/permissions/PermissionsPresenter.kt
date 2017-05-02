package com.develop.zuzik.redux.model.permissions

import com.develop.zuzik.redux.core.ReduxPresenter
import com.develop.zuzik.redux.core.extension.asConsumer

/**
 * User: zuzik
 * Date: 5/1/17
 */
class PermissionsPresenter(private val model: Permissions.Model) :
		ReduxPresenter<Permissions.View>(),
		Permissions.Presenter {

	override fun onStart(view: Permissions.View) {
		intent(model
				.permissionRequest
				.subscribe(view.requestPermissions.asConsumer()))

		intent(view
				.onReceivePermissionsResponse
				.subscribe(model.onReceivePermissionResponse.asConsumer()))
	}
}