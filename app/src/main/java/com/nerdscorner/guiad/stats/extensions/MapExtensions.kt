package com.nerdscorner.guiad.stats.extensions

fun <K : Comparable<K>, V> Map<K, V>.sortIf(condition: Boolean): Map<K, V> {
    return if (condition) {
        toSortedMap()
    } else {
        this
    }
}
