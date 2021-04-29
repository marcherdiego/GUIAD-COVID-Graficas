package com.nerdscorner.covid.stats.domain

data class Stat(val name: String, val index: Int = DUMMY, var selected: Boolean = false) {
    override fun toString() = name

    fun toggle() {
        selected = selected.not()
    }

    fun isDummy() = index == DUMMY

    companion object {
        private const val DUMMY = -1
    }
}
