package com.develop.zuzik.redux.core

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Created by yaroslavzozulia on 9/24/17.
 */
//TODO: reducer should register action class and forget about instanceof
class ReduxStoreTest {

	@Test
	fun bindSendsStateUpdatedByReducers() {
		val state = 0
		val actions = listOf(Observable.just(PlusOneAction(), PlusOneAction(), MinusOneAction()))
		val reducers = listOf(PlusOneReducer(), MinusOneReducer())
		val middlewares = listOf<Middleware<Int>>()
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<Int>()

		store.bind().subscribe(testObserver)

		testObserver.assertValues(0, 1, 2, 1)
		testObserver.assertComplete()
	}

	@Test
	fun bindCallsReducerInOrder() {
		val firstAction = mock(Action::class.java)
		val secondAction = mock(Action::class.java)

		val firstReducer = mock(Reducer::class.java) as Reducer<Int>
		val secondReducer = mock(Reducer::class.java) as Reducer<Int>
		val thirdReducer = mock(Reducer::class.java) as Reducer<Int>

		`when`(firstReducer.reduce(0, firstAction)).thenReturn(1)
		`when`(secondReducer.reduce(1, firstAction)).thenReturn(2)
		`when`(thirdReducer.reduce(2, firstAction)).thenReturn(3)
		`when`(firstReducer.reduce(3, secondAction)).thenReturn(4)
		`when`(secondReducer.reduce(4, secondAction)).thenReturn(5)
		`when`(thirdReducer.reduce(5, secondAction)).thenReturn(6)

		val inOrder = inOrder(firstReducer, secondReducer, thirdReducer)

		val state = 0
		val actions = listOf(Observable.just<Action>(firstAction, secondAction))
		val reducers = listOf(firstReducer, secondReducer, thirdReducer)
		val middlewares = listOf<Middleware<Int>>()
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<Int>()

		store.bind().subscribe(testObserver)

		testObserver.assertValues(0, 3, 6)
		testObserver.assertComplete()

		inOrder.verify(firstReducer, times(1)).reduce(0, firstAction)
		inOrder.verify(secondReducer, times(1)).reduce(1, firstAction)
		inOrder.verify(thirdReducer, times(1)).reduce(2, firstAction)
		inOrder.verify(firstReducer, times(1)).reduce(3, secondAction)
		inOrder.verify(secondReducer, times(1)).reduce(4, secondAction)
		inOrder.verify(thirdReducer, times(1)).reduce(5, secondAction)
	}

	@Test
	fun bindSendsErrorIfReducerCausesError() {
		val state = 0
		val actions = listOf(Observable.just(PlusOneAction(), ErrorAction()))
		val reducers = listOf(PlusOneReducer(), ErrorReducer())
		val middlewares = listOf<Middleware<Int>>()
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<Int>()

		store.bind().subscribe(testObserver)

		testObserver.assertValues(0, 1)
		testObserver.assertError(ReducerException::class.java)
	}

	@Test
	fun bindDoesNotSendStateIfItIsSameWithPreviousState() {
		val state = 0
		val actions = listOf(Observable.just(PlusOneAction(), DoNothingAction()))
		val reducers = listOf(PlusOneReducer(), DoNothingReducer())
		val middlewares = listOf<Middleware<Int>>()
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<Int>()

		store.bind().subscribe(testObserver)

		testObserver.assertValues(0, 1)
		testObserver.assertComplete()
	}

	@Test
	fun bindDoesNotSendStateIfItIsEqualToPreviousState() {
		val state = 0
		val actions = listOf(Observable.just(PlusOneAction(), CopyAction()))
		val reducers = listOf(PlusOneReducer(), CopyReducer())
		val middlewares = listOf<Middleware<Int>>()
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<Int>()

		store.bind().subscribe(testObserver)

		testObserver.assertValues(0, 1)
		testObserver.assertComplete()
	}

	@Test
	fun bindCallsMiddlewares() {
		val filteredMinusOneAction = MinusOneAction()
		val filteredSetZeroAction = SetZeroAction()
		val state = 0
		val actions = listOf(Observable.just(PlusOneAction(), filteredMinusOneAction, PlusOneAction(), filteredSetZeroAction))
		val reducers = listOf(PlusOneReducer(), MinusOneReducer(), SetZeroReducer())
		val middlewares = listOf<Middleware<Int>>(FilterActionMiddleware(filteredMinusOneAction), FilterActionMiddleware(filteredSetZeroAction))
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<Int>()

		store.bind().subscribe(testObserver)

		testObserver.assertValues(0, 1, 2)
		testObserver.assertComplete()
	}

	@Test
	fun bindCallsMiddlewaresInOrder() {
		fail("implement")
	}

	@Test
	fun bindSendsErrorIfMiddlewareCausesError() {
		val state = 0
		val actions = listOf(Observable.just(PlusOneAction(), ErrorAction()))
		val reducers = listOf(PlusOneReducer())
		val middlewares = listOf<Middleware<Int>>(ErrorMiddleware())
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<Int>()

		store.bind().subscribe(testObserver)

		testObserver.assertValues(0, 1)
		testObserver.assertError(MiddlewareException::class.java)
	}

	private class PlusOneAction : Action
	private class MinusOneAction : Action
	private class SetZeroAction : Action
	private class ErrorAction : Action
	private class CopyAction : Action
	private class DoNothingAction : Action

	private class PlusOneReducer : Reducer<Int> {
		override fun reduce(oldState: Int, action: Action): Int =
				(action as? PlusOneAction)?.let { oldState + 1 } ?: oldState
	}

	private class MinusOneReducer : Reducer<Int> {
		override fun reduce(oldState: Int, action: Action): Int =
				(action as? MinusOneAction)?.let { oldState - 1 } ?: oldState
	}

	private class SetZeroReducer : Reducer<Int> {
		override fun reduce(oldState: Int, action: Action): Int =
				(action as? SetZeroAction)?.let { 0 } ?: oldState
	}

	private class ErrorReducer : Reducer<Int> {
		override fun reduce(oldState: Int, action: Action): Int =
				(action as? ErrorAction)?.let { throw ReducerException() } ?: oldState
	}

	private class CopyReducer : Reducer<Int> {
		override fun reduce(oldState: Int, action: Action): Int =
				(action as? CopyAction)?.let { oldState + 0 } ?: oldState
	}

	private class DoNothingReducer : Reducer<Int> {
		override fun reduce(oldState: Int, action: Action): Int =
				(action as? DoNothingAction)?.let { oldState } ?: oldState
	}

	private class ReducerException : Exception()
	private class MiddlewareException : Exception()

	private class FilterActionMiddleware(private val filteredAction: Action) : Middleware<Int> {

		override fun dispatch(state: Observable<Int>, action: Action): Observable<Int> =
				state.filter { action !== filteredAction }
	}

	private class ErrorMiddleware : Middleware<Int> {

		override fun dispatch(state: Observable<Int>, action: Action): Observable<Int> =
				state.map { if (action is ErrorAction) throw MiddlewareException() else it }
	}
}