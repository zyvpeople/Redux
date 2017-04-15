package com.develop.zuzik.redux.sample.editabledata

import com.develop.zuzik.redux.model.editabledata.UpdateDataCommand
import com.develop.zuzik.redux.sample.entity.User
import io.reactivex.Single
import java.util.concurrent.TimeUnit

/**
 * User: zuzik
 * Date: 4/15/17
 */
class UpdateUserCommand : UpdateDataCommand<User> {

	private var counter = 0

	override fun update(data: User): Single<User> =
			when ((counter++) % 3) {
				0 -> Single.just(data)
				1 -> Single.just(data)
				else -> Single.error(RuntimeException("Error saving user"))
			}.delay(2, TimeUnit.SECONDS)
}