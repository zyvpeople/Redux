package com.develop.zuzik.redux.model.readonlydata

import com.develop.zuzik.redux.core.Version

/**
 * User: zuzik
 * Date: 4/15/17
 */
data class ReadOnlyDataState<Data>(val data: Version<Data>,
								   val loading: Boolean,
								   val error: Throwable?)