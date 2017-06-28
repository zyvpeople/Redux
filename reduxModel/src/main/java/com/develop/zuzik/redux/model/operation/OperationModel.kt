package com.develop.zuzik.redux.model.operation

import com.develop.zuzik.redux.core.ReduxModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
class OperationModel<Data, Progress>(private val operationCommand: OperationCommand<Data, Progress>) :
		Operation.Model<Data, Progress>,
		ReduxModel<OperationState<Data, Progress>>(OperationState.Waiting()) {

	override val execute: PublishSubject<Data> = PublishSubject.create()
	override val reset: PublishSubject<Unit> = PublishSubject.create()

	init {
		val executeUserAction = execute
				.map { UserAction.Execute(it) }
		val resetUserAction = reset
				.withLatestFrom<OperationState<Data, Progress>, UserAction<Data>>(state, BiFunction { _, state ->
					resetToUserAction(state)
				})

		addAction(
				Observable
						.merge(
								executeUserAction,
								resetUserAction)
						.switchMap(this::handleUserAction))

		addReducer(OperationReducer())
	}

	private fun resetToUserAction(state: OperationState<Data, Progress>): UserAction<Data> =
			when (state) {
				is OperationState.Progress -> UserAction.Cancel(state.data)
				is OperationState.Waiting,
				is OperationState.Success,
				is OperationState.Fail,
				is OperationState.Canceled -> UserAction.Clear()
			}

	private fun handleUserAction(userAction: UserAction<Data>): Observable<OperationAction<Data, Progress>> =
			when (userAction) {
				is UserAction.Execute ->
					operationCommand
							.execute(userAction.data)
							.map(this::operationCommandStateToAction)
							.onErrorReturn { OperationAction.SetFail(userAction.data, it) }
				is UserAction.Cancel ->
					Observable.just(OperationAction.Cancel(userAction.data))
				is UserAction.Clear ->
					Observable.just(OperationAction.Wait())
			}

	private fun operationCommandStateToAction(state: OperationCommandState<Data, Progress>): OperationAction<Data, Progress> =
			when (state) {
				is OperationCommandState.Progress -> OperationAction.SetProgress(state.data, state.progress)
				is OperationCommandState.Success -> OperationAction.SetSuccess(state.data)
				is OperationCommandState.Fail -> OperationAction.SetFail(state.data, state.error)
			}
}