package com.nerdscorner.guiad.stats.utils

import android.util.Log

object RangeUtils {
    private const val LAST_7_DAYS = "Últimos 7 días"
    private const val LAST_14_DAYS = "Últimos 14 días"
    private const val LAST_30_DAYS = "Últimos 30 días"
    private const val LAST_60_DAYS = "Últimos 60 días"
    private const val LAST_90_DAYS = "Últimos 90 días"
    private const val ALL_RANGE = "Ver todo"

    val dateRanges = listOf(LAST_7_DAYS, LAST_14_DAYS, LAST_30_DAYS, LAST_60_DAYS, LAST_90_DAYS, ALL_RANGE)

    fun getDaysCountForRangeIndex(index: Int, default: Int): Int {
        return when (index) {
            0 -> 7
            1 -> 14
            2 -> 30
            3 -> 60
            4 -> 90
            else -> {
                Log.e("RangeUtils", "RETURNING DEFAULT RANGE: $default")
                default
            }
        }
    }
}
