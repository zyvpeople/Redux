package com.develop.zuzik.redux.core

/**
 * Created by yaroslavzozulia on 6/20/17.
 */
data class Version<Data>(val version: Int = 0, val data: Data) {

	fun newVersion(data: Data) =
			copy(nextVersion(), data)

	private fun nextVersion(): Int =
			if (version == Int.MAX_VALUE) 0 else version + 1
}