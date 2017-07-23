package com.develop.zuzik.redux.model.lock

import android.util.Log
import com.develop.zuzik.redux.core.ReduxModel
import com.develop.zuzik.redux.model.lock.lock_timer.LockTimer
import com.develop.zuzik.redux.model.lock.lock_timer.never.NeverLockTimer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

/**
 * Created by yaroslavzozulia on 7/4/17.
 */
//TODO: add logic to save last timer time because system can terminate application but timer delay can be very long
class LockModel(private val lockTimer: LockTimer) :
		ReduxModel<LockState>(LockState(locked = false), AndroidSchedulers.mainThread()),
		Lock.Model {

	override val lock: PublishSubject<Unit> = PublishSubject.create()
	override val unlock: PublishSubject<Unit> = PublishSubject.create()
	override val startTimer: PublishSubject<Unit> = PublishSubject.create()
	override val stopTimer: PublishSubject<Unit> = PublishSubject.create()

	init {
		addAction(lock.map { LockAction.Lock() })
		addAction(unlock.map { LockAction.Unlock() })
		addAction(
				Observable
						.merge(
								lock.map { false },
								unlock.map { true },
								startTimer
										.withLatestFrom(state, BiFunction<Unit, LockState, Boolean> { _, state -> !state.locked }),
								stopTimer.map { false })
						.map {
							if (it) {
								lockTimer
							} else {
								NeverLockTimer()
							}
						}
						.switchMap {
							it.lock().toObservable()
						}
						.map { LockAction.Lock() })
		addReducer(LockReducer())
	}
}