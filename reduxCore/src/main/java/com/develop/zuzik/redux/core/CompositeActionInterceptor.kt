package com.develop.zuzik.redux.core

/**
 * User: zuzik
 * Date: 4/15/17
 */
class CompositeActionInterceptor: Function1<Action, Unit> {

	private var interceptors: List<Function1<Action, Unit>> = listOf()

	fun addInterceptor(interceptor: Function1<Action, Unit>) {
		interceptors += interceptor
	}

	override fun invoke(action: Action) {
		interceptors.forEach { it.invoke(action) }
	}
}