package com.develop.zuzik.redux.sample.extension

import android.widget.TextView
import io.reactivex.functions.Consumer

/**
 * User: zuzik
 * Date: 4/15/17
 */

fun TextView.textIfNotEquals(): Consumer<in String> = Consumer {
	if (text.toString() != it) {
		text = it
	}
}