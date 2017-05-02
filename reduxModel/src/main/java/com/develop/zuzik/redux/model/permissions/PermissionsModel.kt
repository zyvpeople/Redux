package com.develop.zuzik.redux.model.permissions

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.ReduxModel
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
		ReduxModel<PermissionsState>(PermissionsState(emptySet())),
		Permissions.Model {

	private val action = PublishSubject.create<Action>()
	private val mapper = PermissionMapper()

	override val onReceivePermissionResponse: PublishSubject<PermissionResponse> = PublishSubject.create()
	override val permissionRequest: PublishSubject<PermissionRequest> = PublishSubject.create()

	init {
		addAction(action)
		addReducer(PermissionsReducer())
	}

	override fun <T> checkPermission(vararg permission: Permission): (Observable<T>) -> ObservableSource<T> = { original ->
		operationId { operationId ->
			val permissions = Observable.just(permission.asList())
			val grantedPermissionResponse = onReceivePermissionResponse
					.startWith(PermissionResponse(
							operationId = operationId,
							grantedPermissions = permission.map { GrantedPermission(it, true) }))
					.filter { it.operationId == operationId }
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
							permissionRequest.onNext(PermissionRequest(
									operationId = operationId,
									permissions = it.map(GrantedPermission::permission)
							))
						}
					}
					.filter { it.isEmpty() }
					.switchMap { original }
		}
				.subscribeOn(AndroidSchedulers.mainThread())
	}

	private fun <T> operationId(operationIdSupplier: (Int) -> Observable<T>): Observable<T> =
			Observable
					.defer {
						state
								.take(1)
					}
					.map {
						val currentMaxId = it.operationIds.max()
						val hypotheticalCurrentIdsRange = (0..(currentMaxId ?: 0))
						val realCurrentIdsRange = it.operationIds
						val unusedIds = hypotheticalCurrentIdsRange.minus(realCurrentIdsRange)
						val newOperationId = unusedIds.firstOrNull() ?: (currentMaxId?.let { it + 1 } ?: 0)
						newOperationId
					}
					.doOnNext { action.onNext(PermissionsAction.RegisterOperation(it)) }
					.flatMap { operationId ->
						Observable
								.using(
										{ operationId },
										{ operationIdSupplier(it) },
										{ action.onNext(PermissionsAction.UnregisterOperation(operationId)) })
					}

	private fun toGrantedPermission(permission: Permission): GrantedPermission =
			GrantedPermission(
					permission = permission,
					granted = mapper
							.mapToString(permission)
							?.let { ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
							?: false)
}