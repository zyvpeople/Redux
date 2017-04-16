package com.develop.zuzik.redux.model.entities

import com.develop.zuzik.redux.core.ReduxPresenter
import com.develop.zuzik.redux.core.extension.UnitInstance
import com.develop.zuzik.redux.core.extension.asConsumer

/**
 * User: zuzik
 * Date: 4/16/17
 */
class EntitiesPresenter<Entity, Filter>(private val model: Entities.Model<Entity, Filter>) :
		ReduxPresenter<Entities.View<Entity, Filter>>(),
		Entities.Presenter<Entity, Filter> {

	override fun onStart(view: Entities.View<Entity, Filter>) {
		intent(model
				.state
				.map { it.loading }
				.subscribe(view.displayProgress.asConsumer()))
		intent(model
				.state
				.map { it.entities }
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