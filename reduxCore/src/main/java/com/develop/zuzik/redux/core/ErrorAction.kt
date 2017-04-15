package com.develop.zuzik.redux.core

/**
 * User: zuzik
 * Date: 4/15/17
 */
interface ErrorAction : Action {
	val error: Throwable
}