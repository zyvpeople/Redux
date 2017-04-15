package com.develop.zuzik.redux.model.readonlydata

import io.reactivex.Observable

/**
 * User: zuzik
 * Date: 4/15/17
 */
interface DataQuery<Data> {
	fun query(): Observable<Data>
}