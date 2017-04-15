package com.develop.zuzik.redux.model.editabledata

import io.reactivex.Single

/**
 * User: zuzik
 * Date: 4/15/17
 */
interface UpdateDataCommand<Data> {
	fun update(data: Data): Single<Data>
}