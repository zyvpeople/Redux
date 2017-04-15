package com.develop.zuzik.redux.model.readonlydata

/**
 * User: zuzik
 * Date: 4/15/17
 */
data class ReadOnlyDataState<out Data>(val data: Data,
									   val loading: Boolean,
									   val error: Throwable?)