package com.nerdscorner.guiad.stats.domain

data class Stat(
    var name: String = "",
    val index: Int = DUMMY,
    var selected: Boolean = false,
    val factor: Float = DEFAULT_FACTOR,
    val isSorted: Boolean = false
) {
    override fun toString() = name

    fun toggle() {
        selected = selected.not()
    }

    fun isDummy() = index == DUMMY

    companion object {
        private const val DUMMY = -1
        const val DEFAULT_FACTOR = 1f
    }
}
