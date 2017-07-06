package com.develop.zuzik.redux.sample.lock

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.core.extension.UnitInstance
import com.develop.zuzik.redux.model.lock.Lock
import com.develop.zuzik.redux.model.lock.LockModel
import com.develop.zuzik.redux.model.lock.lock_timer.delay.Delay
import com.develop.zuzik.redux.model.lock.lock_timer.delay.DelayLockTimer
import com.develop.zuzik.redux.model.lock.lock_timer.delay.DelaySettings
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_lock.*
import java.util.concurrent.TimeUnit

class LockActivity : AppCompatActivity() {

	companion object Models {
		val delaySettings = DelaySettings(Delay.Real(10, TimeUnit.SECONDS))
		val lockModel: Lock.Model by lazy {
			val model = LockModel(DelayLockTimer(delaySettings))
			model.init()
			model
		}
	}

	private val compositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_lock)
		lockModel.startTimer.onNext(UnitInstance.INSTANCE)
	}

	override fun onDestroy() {
		lockModel.stopTimer.onNext(UnitInstance.INSTANCE)
		super.onDestroy()
	}

	override fun onStart() {
		super.onStart()
		intent(delaySettings
				.delay
				.subscribe(this::saveDelayToRepository))
		intent(lockModel
				.state
				.subscribe {
					if (it.locked) {
						startActivity(Intent(this, PinCodeActivity::class.java))
					}
				})
		intent(delaySettings
				.delay
				.map(this::delayToString)
				.map { "Current delay: $it" }
				.subscribe(tvCurrentDelay.text()))
		intent(btnSetDelay
				.clicks()
				.map { etDelay.text.toString() }
				.map(this::textToDelay)
				.subscribe { delaySettings.delay.onNext(it) })
	}

	override fun onStop() {
		compositeDisposable.clear()
		super.onStop()
	}

	override fun onUserInteraction() {
		super.onUserInteraction()
		lockModel.startTimer.onNext(UnitInstance.INSTANCE)
	}

	private fun intent(disposable: Disposable) {
		compositeDisposable.add(disposable)
	}

	private fun delayToString(delay: Delay): String =
			when (delay) {
				is Delay.Never -> "Never"
				is Delay.Real -> "${delay.value} ${delay.timeUnit}"
			}

	private fun textToDelay(it: String) = it.toLongOrNull()?.let { Delay.Real(it, TimeUnit.SECONDS) } ?: Delay.Never()

	private fun saveDelayToRepository(delay: Delay) {
		//TODO: here you can save delay
	}
}
