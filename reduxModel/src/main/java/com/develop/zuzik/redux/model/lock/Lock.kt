package com.develop.zuzik.redux.model.lock

import com.develop.zuzik.redux.core.Redux
import io.reactivex.Observer

/**
 * Created by yaroslavzozulia on 7/4/17.
 */
interface Lock {

	interface Model : Redux.Model<LockState> {
		val lock: Observer<Unit>
		val unlock: Observer<Unit>
		val startTimer: Observer<Unit>
		val stopTimer: Observer<Unit>
	}
}