package com.develop.zuzik.redux.model.permissions

import com.develop.zuzik.redux.core.model.ReduxPresenter
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
				.state
				.filter { it.currentRequest == null }
				.filter { it.requests.isNotEmpty() }
				.map { it.requests.entries.first().value }
				.doOnNext { model.onRequestPermissions.onNext(it) }
				.subscribe(view.requestPermissions.asConsumer()))

		intent(view
				.onReceivePermissionsResponse
				.subscribe(model.onReceivePermissionResponse.asConsumer()))
	}
}