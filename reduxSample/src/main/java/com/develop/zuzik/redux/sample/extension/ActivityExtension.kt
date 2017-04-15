package com.develop.zuzik.redux.sample.extension

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import io.reactivex.functions.Consumer

/**
 * User: zuzik
 * Date: 4/15/17
 */

fun Activity.startActivity(activityClass: Class<*>) = Consumer<Unit> {
	startActivity(Intent(this, activityClass))
}

fun Activity.showToast() = Consumer<String> {
	Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
}

fun Activity.showErrorToast() = Consumer<Throwable> {
	Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
}