package com.develop.zuzik.redux.core

/**
 * Created by yaroslavzozulia on 6/20/17.
 */
data class Version<Property>(val version: Int, val property: Property) {

    fun newVersion(property: Property) =
            copy(nextVersion(), property)

    private fun nextVersion(): Int =
            if (version == Int.MAX_VALUE) 0 else version + 1
}