package com.develop.zuzik.redux.core.extension

import io.reactivex.Observer
import io.reactivex.functions.Consumer

/**
 * User: zuzik
 * Date: 4/15/17
 */

fun <T> Observer<T>.asConsumer(): Consumer<T> = Consumer { onNext(it) }