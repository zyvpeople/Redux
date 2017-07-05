package com.develop.zuzik.redux.model.lock.lock_timer

import io.reactivex.Single

/**
 * Created by yaroslavzozulia on 7/5/17.
 */
interface LockTimer {
	fun lock(): Single<Unit>
}