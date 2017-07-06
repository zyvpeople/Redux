package com.develop.zuzik.redux.model.lock.lock_timer.delay

import io.reactivex.subjects.BehaviorSubject

/**
 * Created by yaroslavzozulia on 7/5/17.
 */
class DelaySettings(defaultDelay: Delay) {

	val delay = BehaviorSubject.createDefault(defaultDelay)

}