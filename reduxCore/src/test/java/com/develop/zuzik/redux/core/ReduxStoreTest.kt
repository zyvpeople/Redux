package com.develop.zuzik.redux.core

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import org.mockito.InOrder
import org.mockito.Mockito
import org.mockito.Mockito.*

/**
 * Created by yaroslavzozulia on 9/24/17.
 */
//TODO: reducer should register action class and forget about instanceof
class ReduxStoreTest {

	@Test
	fun bindSendsStateUpdatedByReducers() {
		val state = ""
		val actions = listOf(Observable.just(AddLetterAAction(), AddLetterBAction(), AddLetterCAction()))
		val reducers = listOf(AddLetterAReducer(), AddLetterBReducer(), AddLetterCReducer())
		val middlewares = listOf<Middleware<String>>()
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<String>()

		store.bind(Schedulers.trampoline()).subscribe(testObserver)

		testObserver.assertValues("", "A", "AB", "ABC")
		testObserver.assertComplete()
	}

	@Test
	fun bindCallsReducerInOrder() {
		val firstAction = AddLetterAAction()
		val secondAction = AddLetterBAction()

		val firstReducer = spy(AddLetterAReducer())
		val secondReducer = spy(AddLetterBReducer())
		val thirdReducer = spy(AddLetterCReducer())

		val inOrder = inOrder(firstReducer, secondReducer, thirdReducer)

		val state = ""
		val actions = listOf(Observable.just(firstAction, secondAction))
		val reducers = listOf(firstReducer, secondReducer, thirdReducer)
		val middlewares = listOf<Middleware<String>>()
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<String>()

		store.bind(Schedulers.trampoline()).subscribe(testObserver)

		testObserver.assertValues("", "A", "AB")
		testObserver.assertComplete()

		inOrder.verify(firstReducer, times(1)).reduce("", firstAction)
		inOrder.verify(secondReducer, times(1)).reduce("A", firstAction)
		inOrder.verify(thirdReducer, times(1)).reduce("A", firstAction)
		inOrder.verify(firstReducer, times(1)).reduce("A", secondAction)
		inOrder.verify(secondReducer, times(1)).reduce("A", secondAction)
		inOrder.verify(thirdReducer, times(1)).reduce("AB", secondAction)

		verifyNoMoreInteractions(firstReducer)
		verifyNoMoreInteractions(secondReducer)
		verifyNoMoreInteractions(thirdReducer)
	}

	@Test
	fun bindSendsErrorIfReducerCausesError() {
		val state = ""
		val actions = listOf(Observable.just(AddLetterAAction(), ErrorAction()))
		val reducers = listOf(AddLetterAReducer(), ErrorReducer())
		val middlewares = listOf<Middleware<String>>()
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<String>()

		store.bind(Schedulers.trampoline()).subscribe(testObserver)

		testObserver.assertValues("", "A")
		testObserver.assertError(ReducerException::class.java)
	}

	@Test
	fun bindDoesNotSendStateIfItIsSameWithPreviousState() {
		val state = ""
		val actions = listOf(Observable.just(AddLetterAAction(), DoNothingAction()))
		val reducers = listOf(AddLetterAReducer(), DoNothingReducer())
		val middlewares = listOf<Middleware<String>>()
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<String>()

		store.bind(Schedulers.trampoline()).subscribe(testObserver)

		testObserver.assertValues("", "A")
		testObserver.assertComplete()
	}

	@Test
	fun bindDoesNotSendStateIfItIsEqualToPreviousState() {
		val state = ""
		val actions = listOf(Observable.just(AddLetterAAction(), CopyAction()))
		val reducers = listOf(AddLetterAReducer(), CopyReducer())
		val middlewares = listOf<Middleware<String>>()
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<String>()

		store.bind(Schedulers.trampoline()).subscribe(testObserver)

		testObserver.assertValues("", "A")
		testObserver.assertComplete()
	}

	@Test
	fun bindCallsMiddlewares() {
		val filteredAddLetterBAction = AddLetterBAction()
		val filteredAddLetterCAction = AddLetterCAction()
		val state = ""
		val actions = listOf(Observable.just(AddLetterAAction(), filteredAddLetterBAction, AddLetterAAction(), filteredAddLetterCAction))
		val reducers = listOf(AddLetterAReducer(), AddLetterBReducer(), AddLetterCReducer())
		val middlewares = listOf<Middleware<String>>(FilterActionMiddleware(filteredAddLetterBAction), FilterActionMiddleware(filteredAddLetterCAction))
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<String>()

		store.bind(Schedulers.trampoline()).subscribe(testObserver)

		testObserver.assertValues("", "A", "AA")
		testObserver.assertComplete()
	}

	@Test
	fun bindCallsMiddlewaresInOrder() {
		val mockAction = mock(Action::class.java)

		val spyMiddlewareA = AddTextMiddleware("A")
		val spyMiddlewareB = AddTextMiddleware("B")
		val spyMiddlewareC = AddTextMiddleware("C")

		val state = ""
		val actions = listOf(Observable.just(mockAction))
		val reducers = listOf<Reducer<String>>()
		val middlewares = listOf(spyMiddlewareA, spyMiddlewareB, spyMiddlewareC)
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<String>()

		store.bind(Schedulers.trampoline()).subscribe(testObserver)

		testObserver.assertValues("", "ABC")
		testObserver.assertComplete()
	}

	@Test
	fun bindSendsErrorIfMiddlewareCausesError() {
		val state = ""
		val actions = listOf(Observable.just(AddLetterAAction(), ErrorAction()))
		val reducers = listOf(AddLetterAReducer())
		val middlewares = listOf<Middleware<String>>(ErrorMiddleware())
		val store = ReduxStore(state, actions, reducers, middlewares)
		val testObserver = TestObserver<String>()

		store.bind(Schedulers.trampoline()).subscribe(testObserver)

		testObserver.assertValues("", "A")
		testObserver.assertError(MiddlewareException::class.java)
	}

	private class AddLetterAAction : Action
	private class AddLetterBAction : Action
	private class AddLetterCAction : Action
	private class ErrorAction : Action
	private class CopyAction : Action
	private class DoNothingAction : Action

	private open class AddLetterAReducer : Reducer<String> {
		override fun reduce(oldState: String, action: Action): String =
				(action as? AddLetterAAction)?.let { oldState + "A" } ?: oldState
	}

	private open class AddLetterBReducer : Reducer<String> {
		override fun reduce(oldState: String, action: Action): String =
				(action as? AddLetterBAction)?.let { oldState + "B" } ?: oldState
	}

	private open class AddLetterCReducer : Reducer<String> {
		override fun reduce(oldState: String, action: Action): String =
				(action as? AddLetterCAction)?.let { oldState + "C" } ?: oldState
	}

	private class ErrorReducer : Reducer<String> {
		override fun reduce(oldState: String, action: Action): String =
				(action as? ErrorAction)?.let { throw ReducerException() } ?: oldState
	}

	private class CopyReducer : Reducer<String> {
		override fun reduce(oldState: String, action: Action): String =
				(action as? CopyAction)?.let { oldState + "" } ?: oldState
	}

	private class DoNothingReducer : Reducer<String> {
		override fun reduce(oldState: String, action: Action): String =
				(action as? DoNothingAction)?.let { oldState } ?: oldState
	}

	private class ReducerException : Exception()
	private class MiddlewareException : Exception()

	private class FilterActionMiddleware(private val filteredAction: Action) : Middleware<String> {

		override fun dispatch(state: Observable<String>, action: Action): Observable<String> =
				state.filter { action !== filteredAction }
	}

	private class ErrorMiddleware : Middleware<String> {

		override fun dispatch(state: Observable<String>, action: Action): Observable<String> =
				state.map { if (action is ErrorAction) throw MiddlewareException() else it }
	}

	private open class AddTextMiddleware(private val text: String) : Middleware<String> {

		override fun dispatch(state: Observable<String>, action: Action): Observable<String> =
				state.map { it + text }
	}
}