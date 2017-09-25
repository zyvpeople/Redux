package com.develop.zuzik.redux.core.store

/**
 * User: zuzik
 * Date: 4/15/17
 */
interface Reducer<State> {
	fun reduce(oldState: State, action: Action): State
}