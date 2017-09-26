package com.develop.zuzik.redux.core.store.reducer

import com.develop.zuzik.redux.core.store.Action
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by yaroslavzozulia on 9/26/17.
 */
class ActionReducerTest {

	@Test
	fun reduce() {
		assertEquals("A", AddLetterAReducer().reduce("", AddLetterAAction()))
		assertEquals("", AddLetterAReducer().reduce("", AddLetterBAction()))
	}

	private class AddLetterAAction : Action
	private class AddLetterBAction : Action

	private class AddLetterAReducer : ActionReducer<String, AddLetterAAction>(AddLetterAAction::class.java) {
		override fun reduceAction(oldState: String, action: AddLetterAAction): String = oldState + "A"
	}
}