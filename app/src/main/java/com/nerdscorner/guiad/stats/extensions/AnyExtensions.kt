package com.nerdscorner.guiad.stats.extensions

fun <T> T?.toList(): List<T>? {
    return if (this == null) {
        null
    } else {
        listOf(this)
    }
}
