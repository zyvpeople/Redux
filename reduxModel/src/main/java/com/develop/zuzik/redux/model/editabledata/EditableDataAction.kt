package com.develop.zuzik.redux.model.editabledata

import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.model.ErrorAction

/**
 * User: zuzik
 * Date: 4/15/17
 */
internal sealed class EditableDataAction<Data> : Action {
	class BeginLoading<Data> : EditableDataAction<Data>()
	class Load<Data>(val data: Data) : EditableDataAction<Data>()
	class Edit<Data> : EditableDataAction<Data>()
	class CancelEdit<Data> : EditableDataAction<Data>()
	class ModifyEditedData<Data>(val editedData: Data) : EditableDataAction<Data>()
	class BeginSaving<Data> : EditableDataAction<Data>()
	class Save<Data>(val data: Data) : EditableDataAction<Data>()
	class HandleLoadingError<Data>(override val error: Throwable) : EditableDataAction<Data>(), ErrorAction
	class HandleSavingError<Data>(override val error: Throwable) : EditableDataAction<Data>(), ErrorAction
}