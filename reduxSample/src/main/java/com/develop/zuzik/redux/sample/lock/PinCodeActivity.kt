package com.develop.zuzik.redux.sample.lock

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.core.extension.UnitInstance
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.activity_pin_code.*

class PinCodeActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pin_code)
		btnOk
				.clicks()
				.map { etPinCode.text.toString() }
				.subscribe {
					if (it == "1111") {
						LockActivity.lockModel.unlock.onNext(UnitInstance.INSTANCE)
					}
					finish()
				}
	}
}
