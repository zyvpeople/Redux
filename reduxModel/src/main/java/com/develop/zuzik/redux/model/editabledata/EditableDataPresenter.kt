package com.develop.zuzik.redux.model.editabledata

import com.develop.zuzik.redux.core.ReduxPresenter
import com.develop.zuzik.redux.core.extension.UnitInstance
import com.develop.zuzik.redux.core.extension.asConsumer

/**
 * User: zuzik
 * Date: 4/15/17
 */
class EditableDataPresenter<Data>(private val model: EditableData.Model<Data>) :
		ReduxPresenter<EditableData.View<Data>>(),
		EditableData.Presenter<Data> {

	override fun onStart(view: EditableData.View<Data>) {
		intent(model
				.property { it.loading }
				.subscribe(view.displayProgress.asConsumer()))
		intent(model
				.versionProperty { it.editedData }
				.subscribe(view.displayData.asConsumer()))
		intent(model
				.property { it.editing }
				.subscribe(view.setDataEditable.asConsumer()))
		intent(model
				.property { !it.editing }
				.subscribe(view.allowRefresh.asConsumer()))
		intent(model
				.property { !it.editing }
				.subscribe(view.allowEdit.asConsumer()))
		intent(model
				.property { it.editing }
				.subscribe(view.allowCancelEdit.asConsumer()))
		intent(model
				.property { it.editing }
				.subscribe(view.allowSave.asConsumer()))

		intent(model
				.state
				.filter { it.error != null }
				.map { it.error!! }
				.subscribe(view.displayError.asConsumer()))
		intent(model
				.state
				.filter { it.error == null }
				.map { UnitInstance.INSTANCE }
				.subscribe(view.hideError.asConsumer()))
		intent(model
				.error
				.subscribe(view.displayErrorNotification.asConsumer()))

		intent(view
				.onRefresh
				.subscribe(model.refresh.asConsumer()))
		intent(view
				.onEdit
				.subscribe(model.edit.asConsumer()))
		intent(view
				.onCancelEdit
				.subscribe(model.cancelEdit.asConsumer()))
		intent(view
				.onModify
				.subscribe(model.modify.asConsumer()))
		intent(view
				.onSave
				.subscribe(model.save.asConsumer()))
	}
}