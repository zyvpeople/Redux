package com.develop.zuzik.redux.model.readonlyentities

import com.develop.zuzik.redux.core.model.ReduxPresenter
import com.develop.zuzik.redux.core.extension.UnitInstance
import com.develop.zuzik.redux.core.extension.asConsumer

/**
 * User: zuzik
 * Date: 4/16/17
 */
class ReadOnlyEntitiesPresenter<Entity, Filter>(private val model: ReadOnlyEntities.Model<Entity, Filter>) :
		ReduxPresenter<ReadOnlyEntities.View<Entity, Filter>>(),
		ReadOnlyEntities.Presenter<Entity, Filter> {

	override fun onStart(view: ReadOnlyEntities.View<Entity, Filter>) {
		intent(model
				.state
				.map { it.loading }
				.subscribe(view.displayProgress.asConsumer()))
		intent(model
				.versionProperty { it.entities }
				.subscribe(view.displayEntities.asConsumer()))
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
				.onFilter
				.subscribe(model.filter.asConsumer()))
	}
}