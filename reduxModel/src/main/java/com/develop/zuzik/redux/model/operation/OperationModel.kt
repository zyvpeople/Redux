package com.develop.zuzik.redux.model.operation

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Middleware
import com.develop.zuzik.redux.core.ReduxModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
class OperationModel<Input, Output, Progress>(private val operationCommand: OperationCommand<Input, Output, Progress>) :
		Operation.Model<Input, Output, Progress>,
		ReduxModel<OperationState<Input, Output, Progress>>(
				OperationState.Waiting(),
				AndroidSchedulers.mainThread()) {

	override val execute: PublishSubject<Input> = PublishSubject.create()
	override val reset: PublishSubject<Unit> = PublishSubject.create()

	override val success: PublishSubject<Output> = PublishSubject.create()
	override val fail: PublishSubject<Pair<Input, Throwable>> = PublishSubject.create()
	override val canceled: PublishSubject<Input> = PublishSubject.create()

	init {
		val executeUserAction = execute
				.map { UserAction.Execute(it) }
		val resetUserAction = reset
				.withLatestFrom<OperationState<Input, Output, Progress>, UserAction<Input>>(state, BiFunction { _, state ->
					resetToUserAction(state)
				})

		addAction(
				Observable
						.merge(
								executeUserAction,
								resetUserAction)
						.switchMap(this::handleUserAction))
		addReducer(OperationReducer())
		addMiddleware(object: Middleware<OperationState<Input, Output, Progress>> {
			override fun dispatch(state: Observable<OperationState<Input, Output, Progress>>, action: Action): Observable<OperationState<Input, Output, Progress>> =
					state.doOnNext { (action as? OperationAction.SetSuccess<Input, Output, Progress>)?.let { success.onNext(it.output) } }
		})
		addMiddleware(object: Middleware<OperationState<Input, Output, Progress>> {
			override fun dispatch(state: Observable<OperationState<Input, Output, Progress>>, action: Action): Observable<OperationState<Input, Output, Progress>> =
					state.doOnNext { (action as? OperationAction.SetFail<Input, Output, Progress>)?.let { fail.onNext(it.input to it.error) } }
		})
		addMiddleware(object: Middleware<OperationState<Input, Output, Progress>> {
			override fun dispatch(state: Observable<OperationState<Input, Output, Progress>>, action: Action): Observable<OperationState<Input, Output, Progress>> =
					state.doOnNext { (action as? OperationAction.Cancel<Input, Output, Progress>)?.let { canceled.onNext(it.input) } }
		})
	}

	private fun resetToUserAction(state: OperationState<Input, Output, Progress>): UserAction<Input> =
			when (state) {
				is OperationState.Progress -> UserAction.Cancel(state.input)
				is OperationState.Waiting,
				is OperationState.Success,
				is OperationState.Fail,
				is OperationState.Canceled -> UserAction.Clear()
			}

	private fun handleUserAction(userAction: UserAction<Input>): Observable<OperationAction<Input, Output, Progress>> =
			when (userAction) {
				is UserAction.Execute ->
					operationCommand
							.execute(userAction.input)
							.map(this::operationCommandStateToAction)
							.onErrorReturn { OperationAction.SetFail(userAction.input, it) }
				is UserAction.Cancel ->
					Observable.just(OperationAction.Cancel(userAction.input))
				is UserAction.Clear ->
					Observable.just(OperationAction.Wait())
			}

	private fun operationCommandStateToAction(state: OperationCommandState<Input, Output, Progress>): OperationAction<Input, Output, Progress> =
			when (state) {
				is OperationCommandState.Progress -> OperationAction.SetProgress(state.input, state.progress)
				is OperationCommandState.Success -> OperationAction.SetSuccess(state.output)
				is OperationCommandState.Fail -> OperationAction.SetFail(state.input, state.error)
			}
}