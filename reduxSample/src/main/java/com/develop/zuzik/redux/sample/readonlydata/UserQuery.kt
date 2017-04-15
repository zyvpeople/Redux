package com.develop.zuzik.redux.sample.readonlydata

import com.develop.zuzik.redux.model.readonlydata.DataQuery
import com.develop.zuzik.redux.sample.entity.User
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * User: zuzik
 * Date: 4/15/17
 */
class UserQuery : DataQuery<User> {

	private var counter = 0

	override fun query(): Observable<User> =
			when ((counter++) % 3) {
				0 -> Observable.just(User("First name 0", "Last name 0", "Email 0"))
				1 -> Observable.just(User("First name 1", "Last name 1", "Email 1"))
				else -> Observable.error(RuntimeException("Error loading user"))
			}.delay(2, TimeUnit.SECONDS)
}