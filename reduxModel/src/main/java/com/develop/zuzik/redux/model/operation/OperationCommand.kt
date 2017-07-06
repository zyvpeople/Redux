package com.develop.zuzik.redux.model.operation

import io.reactivex.Observable

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
interface OperationCommand<Input, Output, Progress> {
	fun execute(input: Input): Observable<OperationCommandState<Input, Output, Progress>>
}