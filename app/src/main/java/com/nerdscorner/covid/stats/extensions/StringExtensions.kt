package com.nerdscorner.covid.stats.extensions

fun String.formatNumberString(): String {
    return try {
        String.format("%,d", toLong())
    } catch (e: Exception) {
        this
    }
}
