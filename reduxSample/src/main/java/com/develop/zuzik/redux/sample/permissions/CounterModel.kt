package com.develop.zuzik.redux.sample.permissions

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer
import com.develop.zuzik.redux.core.ReduxModel
import com.develop.zuzik.redux.model.permissions.Permission
import com.develop.zuzik.redux.model.permissions.Permissions
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * User: zuzik
 * Date: 5/1/17
 */
internal class TestModel(private val permissionsModel: Permissions.Model) : ReduxModel<TestState>(TestState()) {

	val increment: PublishSubject<Unit> = PublishSubject.create()
	val decrement: PublishSubject<Unit> = PublishSubject.create()

	init {
		addAction(increment.switchMap { performIncrement() })
		addAction(decrement.switchMap { performDecrement() })
		addReducer(IncrementReducer())
		addReducer(DecrementReducer())
	}

	private fun performIncrement(): Observable<Action> =
			Observable
					.just<Action>(Increment())
					.delay(2, TimeUnit.SECONDS)
					.compose(permissionsModel.checkPermission(Permission.Calendar.WriteCalendar()))

	private fun performDecrement(): Observable<Action> =
			Observable
					.just<Action>(Decrement())
					.delay(2, TimeUnit.SECONDS)
					.compose(permissionsModel.checkPermission(
							Permission.Contacts.GetAccountsContacts(),
							Permission.Microphone.RecordAudio()))
}

internal data class TestState(val counter: Int = 0)

internal class Increment : Action
internal class Decrement : Action

internal class IncrementReducer : Reducer<TestState> {
	override fun reduce(oldState: TestState, action: Action): TestState =
			(action as? Increment)?.let { oldState.copy(counter = oldState.counter + 1) } ?: oldState
}

internal class DecrementReducer : Reducer<TestState> {
	override fun reduce(oldState: TestState, action: Action): TestState =
			(action as? Decrement)?.let { oldState.copy(counter = oldState.counter - 1) } ?: oldState
}