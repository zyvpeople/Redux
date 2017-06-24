package com.develop.zuzik.redux.model.permissions

import com.develop.zuzik.redux.core.Redux
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer

/**
 * User: zuzik
 * Date: 5/1/17
 */
interface Permissions {
	interface Model : Redux.Model<PermissionsState> {

		val onRequestPermissions: Observer<PermissionRequest>
		val onReceivePermissionResponse: Observer<PermissionResponse>

		fun <T> checkPermission(vararg permission: Permission): (Observable<T>) -> ObservableSource<T>
	}

	interface View : Redux.View {
		val requestPermissions: Observer<in PermissionRequest>

		val onReceivePermissionsResponse: Observable<PermissionResponse>
	}

	interface Presenter : Redux.Presenter<View>
}