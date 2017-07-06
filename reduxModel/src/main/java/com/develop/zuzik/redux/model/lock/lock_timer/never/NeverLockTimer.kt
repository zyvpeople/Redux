package com.develop.zuzik.redux.model.lock.lock_timer.never

import com.develop.zuzik.redux.model.lock.lock_timer.LockTimer
import io.reactivex.Single

/**
 * Created by yaroslavzozulia on 7/5/17.
 */
class NeverLockTimer : LockTimer {

	override fun lock(): Single<Unit> =
			Single.never()
}