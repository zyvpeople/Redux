package com.develop.zuzik.redux.model.readonlydata

import com.develop.zuzik.redux.core.ReduxPresenter
import com.develop.zuzik.redux.core.extension.UnitInstance
import com.develop.zuzik.redux.core.extension.asConsumer

/**
 * User: zuzik
 * Date: 4/15/17
 */
class ReadOnlyDataPresenter<Data>(private val model: ReadOnlyData.Model<Data>) :
		ReduxPresenter<ReadOnlyData.View<Data>>(),
		ReadOnlyData.Presenter<Data> {

	override fun onStart(view: ReadOnlyData.View<Data>) {
		intent(model
				.state
				.map { it.loading }
				.subscribe(view.displayProgress.asConsumer()))
		intent(model
				.state
				.map { it.data }
				.subscribe(view.displayData.asConsumer()))
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
	}
}