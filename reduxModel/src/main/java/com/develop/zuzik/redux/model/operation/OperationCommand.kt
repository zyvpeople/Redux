package com.develop.zuzik.redux.model.operation

import io.reactivex.Observable

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
interface OperationCommand<Data, Progress> {
	fun execute(data: Data): Observable<OperationCommandState<Data, Progress>>
}