package com.develop.zuzik.redux.sample.operation

import com.develop.zuzik.redux.model.operation.OperationCommand
import com.develop.zuzik.redux.model.operation.OperationCommandState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
class UploadAlbumOperationCommand : OperationCommand<String, Progress> {

	private var executionCounter = 0

	override fun execute(data: String): Observable<OperationCommandState<String, Progress>> =
			Observable
					.defer {
						executionCounter++
						Observable
								.interval(0, 100, TimeUnit.MILLISECONDS)
					}
					.take(101)
					.flatMap<OperationCommandState<String, Progress>> {
						if (executionCounter % 3 == 0 && it == 50L) {
							Observable.error(RuntimeException("Fake error"))
						} else if (it != 100L) {
							Observable.just(OperationCommandState.Progress(data, Progress(it.toInt(), 100)))
						} else {
							Observable.just(OperationCommandState.Success(data))
						}
					}
}