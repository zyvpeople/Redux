package com.develop.zuzik.redux.model.permissions

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.model.ReduxModel
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

/**
 * User: zuzik
 * Date: 4/30/17
 */
class PermissionsModel(private val context: Context) :
		ReduxModel<PermissionsState>(
				PermissionsState(requests = emptyMap(), currentRequest = null),
				AndroidSchedulers.mainThread()),
		Permissions.Model {

	private val action = PublishSubject.create<Action>()
	private val mapper = PermissionMapper()
	private val requestIdProvider = PermissionRequestIdProvider()

	override val onRequestPermissions: PublishSubject<PermissionRequest> = PublishSubject.create()
	override val onReceivePermissionResponse: PublishSubject<PermissionResponse> = PublishSubject.create()

	init {
		addAction(onRequestPermissions
				.doOnNext { action.onNext(PermissionsAction.SetCurrentRequest(it)) }
				.flatMap<Action> { Observable.never() })
		addAction(onReceivePermissionResponse()
				.doOnNext { action.onNext(PermissionsAction.ClearCurrentRequest(it.id)) }
				.flatMap<Action> { Observable.never() })
		addAction(action)
		addReducer(PermissionsReducer())
	}

	override fun <T> checkPermission(vararg permission: Permission): (Observable<T>) -> ObservableSource<T> = { original ->
		requestId { requestId ->
			val permissions = Observable.just(permission.asList())
			val grantedPermissionResponse = onReceivePermissionResponse()
					.startWith(PermissionResponse(
							id = requestId,
							grantedPermissions = permission.map { GrantedPermission(it, true) }))
					.filter { it.id == requestId }
					.flatMap {
						if (it.grantedPermissions.find { !it.granted } == null) {
							Observable.just(it)
						} else {
							Observable.error(PermissionIsNotGrantedException())
						}
					}

			Observable
					.combineLatest<List<Permission>, PermissionResponse, List<Permission>>(
							permissions,
							grantedPermissionResponse,
							BiFunction { permissions, _ -> permissions })
					.map { permissions ->
						permissions
								.map(this::toGrantedPermission)
								.filter { !it.granted }
					}
					.doOnNext {
						if (it.isNotEmpty()) {
							action.onNext(PermissionsAction.AddRequest(PermissionRequest(
									id = requestId,
									permissions = it.map(GrantedPermission::permission))))
						}
					}
					.filter { it.isEmpty() }
					.switchMap { original }
		}
				.subscribeOn(AndroidSchedulers.mainThread())
	}

	private fun <T> requestId(requestIdSupplier: (Int) -> Observable<T>): Observable<T> =
			Observable
					.defer {
						Observable.just(requestIdProvider.provide())
					}
					.flatMap { requestId ->
						Observable
								.using(
										{ requestId },
										{ requestIdSupplier(it) },
										{ action.onNext(PermissionsAction.RemoveRequest(requestId)) })
					}

	private fun toGrantedPermission(permission: Permission): GrantedPermission =
			GrantedPermission(
					permission = permission,
					granted = mapper
							.mapToString(permission)
							?.let { ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
							?: true)

	private fun onReceivePermissionResponse(): Observable<PermissionResponse> =
			onReceivePermissionResponse
					.filter { it.grantedPermissions.isNotEmpty() }
}