package com.develop.zuzik.redux.model.lock.lock_timer.delay

import com.develop.zuzik.redux.core.extension.UnitInstance
import com.develop.zuzik.redux.model.lock.lock_timer.LockTimer
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by yaroslavzozulia on 7/5/17.
 */
class DelayLockTimer(private val delaySettings: DelaySettings) : LockTimer {

	override fun lock(): Single<Unit> =
			delaySettings
					.delay
					.switchMap(this::delayToObservable)
					.take(1)
					.single(UnitInstance.INSTANCE)

	private fun delayToObservable(delay: Delay): Observable<Unit> =
			when (delay) {
				is Delay.Never -> Observable.never()
				is Delay.Real -> Observable
						.timer(delay.value, delay.timeUnit)
						.map { UnitInstance.INSTANCE }
			}
}