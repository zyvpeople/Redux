package com.develop.zuzik.redux.model.application

import com.develop.zuzik.redux.core.model.Redux

/**
 * Created by yaroslavzozulia on 7/24/17.
 */
interface Application {

	interface Model<Data> : Redux.Model<ApplicationState<Data>>

}