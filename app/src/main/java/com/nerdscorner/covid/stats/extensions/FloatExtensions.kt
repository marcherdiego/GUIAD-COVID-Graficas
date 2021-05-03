package com.nerdscorner.covid.stats.extensions

fun Float.format(digits: Int = 2) = "%.${digits}f".format(this)
