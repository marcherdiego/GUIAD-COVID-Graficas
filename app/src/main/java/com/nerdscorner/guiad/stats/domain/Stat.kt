package com.nerdscorner.guiad.stats.domain

import java.io.Serializable

data class Stat(
    var name: String = "",
    val index: Int,
    var selected: Boolean = false,
    val factor: Float = DEFAULT_FACTOR,
    val isSorted: Boolean = false
) : Serializable {
    override fun toString() = name

    fun toggle() {
        selected = selected.not()
    }

    companion object {
        const val DEFAULT_FACTOR = 1f
    }
}
