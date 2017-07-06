package com.develop.zuzik.redux.model.lock.lock_timer.delay

import java.util.concurrent.TimeUnit

/**
 * Created by yaroslavzozulia on 7/5/17.
 */
sealed class Delay {
	class Never : Delay()
	class Real(val value: Long,
			   val timeUnit: TimeUnit) : Delay()
}