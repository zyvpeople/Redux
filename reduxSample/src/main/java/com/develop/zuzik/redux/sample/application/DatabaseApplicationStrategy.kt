package com.develop.zuzik.redux.sample.application

import com.develop.zuzik.redux.model.application.ApplicationStrategy
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by yaroslavzozulia on 7/24/17.
 */
class DatabaseApplicationStrategy : ApplicationStrategy<Database> {

	override fun initialize(): Single<Database> =
			Single
					.timer(3, TimeUnit.SECONDS)
					.flatMap {
						if (Random().nextBoolean()) {
							Single.just(Database("myDatabase.db"))
						} else {
							Single.error(RuntimeException("Error init database"))
						}
					}
}