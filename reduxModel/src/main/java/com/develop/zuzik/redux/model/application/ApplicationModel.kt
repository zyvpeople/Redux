package com.develop.zuzik.redux.model.application

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.ReduxModel
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by yaroslavzozulia on 7/24/17.
 */
class ApplicationModel<Data>(applicationStrategy: ApplicationStrategy<Data>) :
		ReduxModel<ApplicationState<Data>>(
				defaultState = ApplicationState.Initializing(),
				modelScheduler = AndroidSchedulers.mainThread()),
		Application.Model<Data> {

	init {
		addAction(
				applicationStrategy
						.initialize()
						.map<Action> { ApplicationAction.Initialize(it) }
						.onErrorReturn { ApplicationAction.HandleError<Data>(it) }
						.toObservable())
		addReducer(ApplicationReducer<Data>())
	}
}