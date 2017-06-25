package com.develop.zuzik.redux.model.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.develop.zuzik.redux.core.extension.asObserver
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

/**
 * User: zuzik
 * Date: 5/1/17
 */
class ActivityPermissionsView(private val activity: Activity) : Permissions.View {

	private val mapper = PermissionMapper()

	override val requestPermissions = requestPermissions().asObserver()
	override val onReceivePermissionsResponse: PublishSubject<PermissionResponse> = PublishSubject.create()

	private fun requestPermissions(): Consumer<in PermissionRequest> = Consumer { permissionRequest ->
		permissionRequest
				.permissions
				.map(mapper::mapToString)
				.filter { it != null }
				.map { it!! }
				.toTypedArray()
				.let {
					if (it.isNotEmpty()) {
						ActivityCompat.requestPermissions(
								activity,
								it,
								permissionRequest.id)
					} else {
						Log.e(javaClass.simpleName, "Permissions count is zero")
					}
				}
	}

	fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		onReceivePermissionsResponse
				.onNext(permissions
						.zip(grantResults
								.map { it == PackageManager.PERMISSION_GRANTED })
						.map {
							GrantedPermission(
									permission = mapper.mapFromString(it.first),
									granted = it.second)
						}
						.let {
							PermissionResponse(
									id = requestCode,
									grantedPermissions = it)
						})
	}
}