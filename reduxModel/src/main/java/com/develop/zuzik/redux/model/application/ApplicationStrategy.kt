package com.develop.zuzik.redux.model.application

import io.reactivex.Single

/**
 * Created by yaroslavzozulia on 7/24/17.
 */
interface ApplicationStrategy<Data> {
	fun initialize(): Single<Data>
}