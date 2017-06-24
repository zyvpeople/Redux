package com.develop.zuzik.redux.sample.permissions

import android.util.Log
import com.develop.zuzik.redux.model.entities.EntitiesQuery
import com.develop.zuzik.redux.model.permissions.Permission
import com.develop.zuzik.redux.model.permissions.Permissions
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * User: zuzik
 * Date: 5/2/17
 */
class EntitiesWithPermissionQuery(
		private val permissionsModel: Permissions.Model,
		private val tag: String,
		private val permission: Permission) : EntitiesQuery<String, Unit> {

	override fun query(filter: Unit): Observable<List<String>> =
			Observable
					.just(listOf(Random().nextInt().toString()))
					.doOnNext { Log.d(tag, it.toString()) }
					.compose(permissionsModel.checkPermission(permission))
					.subscribeOn(Schedulers.io())
}