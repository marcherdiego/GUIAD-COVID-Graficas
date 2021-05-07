package com.nerdscorner.guiad.stats.extensions

fun Float.format(digits: Int = 2) = "%.${digits}f".format(this)

fun Float.roundToString(): String {
    return if (this - toInt() == 0f) {
        toInt().toString()
    } else {
        toString()
    }
}
