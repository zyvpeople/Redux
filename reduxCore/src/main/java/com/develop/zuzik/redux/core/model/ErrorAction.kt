package com.develop.zuzik.redux.core.model

import com.develop.zuzik.redux.core.store.Action

/**
 * User: zuzik
 * Date: 4/15/17
 */
interface ErrorAction : Action {
	val error: Throwable
}