package com.develop.zuzik.redux.model.editabledata

import com.develop.zuzik.redux.core.model.value.Version

/**
 * User: zuzik
 * Date: 4/15/17
 */
data class EditableDataState<Data>(val originalData: Data,
								   val editedData: Version<Data>,
								   val loading: Boolean,
								   val editing: Boolean,
								   val error: Throwable?)