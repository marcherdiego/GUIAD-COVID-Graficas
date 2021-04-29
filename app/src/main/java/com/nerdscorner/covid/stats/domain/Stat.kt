package com.nerdscorner.covid.stats.domain

data class Stat(val name: String, val index: Int, var selected: Boolean = false) {
    override fun toString() = name
}
