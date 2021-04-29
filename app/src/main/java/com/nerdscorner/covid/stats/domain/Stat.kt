package com.nerdscorner.covid.stats.domain

data class Stat(val name: String, val index: Int) {
    override fun toString() = name
}
