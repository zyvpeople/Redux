package com.develop.zuzik.redux.core.extension

import io.reactivex.functions.Consumer

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
class CompositeConsumer<Value>(vararg val consumers: Consumer<Value>) : Consumer<Value> {
	override fun accept(value: Value) {
		consumers.forEach { it.accept(value) }
	}
}