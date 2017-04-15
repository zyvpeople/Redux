package com.develop.zuzik.redux.model.editabledata

/**
 * User: zuzik
 * Date: 4/15/17
 */
data class EditableDataState<out Data>(val originalData: Data,
									   val editedData: Data,
									   val loading: Boolean,
									   val editing: Boolean,
									   val error: Throwable?)