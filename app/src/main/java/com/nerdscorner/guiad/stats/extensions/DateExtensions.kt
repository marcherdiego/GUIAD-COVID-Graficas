package com.nerdscorner.guiad.stats.extensions

import java.util.*
import java.util.concurrent.TimeUnit

fun Date.isSameDayOrAfter(other: Date): Boolean {
    val otherCalendar = Calendar.getInstance().apply {
        time = other
    }
    val otherDay = otherCalendar.get(Calendar.DAY_OF_YEAR)
    val otherMonth = otherCalendar.get(Calendar.MONTH)
    val otherYear = otherCalendar.get(Calendar.YEAR)

    val thisCalendar = Calendar.getInstance().apply {
        time = this@isSameDayOrAfter
    }
    val thisDay = thisCalendar.get(Calendar.DAY_OF_YEAR)
    val thisMonth = thisCalendar.get(Calendar.MONTH)
    val thisYear = thisCalendar.get(Calendar.YEAR)

    return if (this.after(other)) {
        true
    } else {
        thisDay == otherDay && thisMonth == otherMonth && thisYear == otherYear
    }
}

fun Date.isSameDayOrBefore(other: Date): Boolean {
    val otherCalendar = Calendar.getInstance().apply {
        time = other
    }
    val otherDay = otherCalendar.get(Calendar.DAY_OF_YEAR)
    val otherMonth = otherCalendar.get(Calendar.MONTH)
    val otherYear = otherCalendar.get(Calendar.YEAR)

    val thisCalendar = Calendar.getInstance().apply {
        time = this@isSameDayOrBefore
    }
    val thisDay = thisCalendar.get(Calendar.DAY_OF_YEAR)
    val thisMonth = thisCalendar.get(Calendar.MONTH)
    val thisYear = thisCalendar.get(Calendar.YEAR)

    return if (this.before(other)) {
        true
    } else {
        thisDay == otherDay && thisMonth == otherMonth && thisYear == otherYear
    }
}

fun Date.getDaysDiff(other: Date?): Long {
    val diffInMillis = this.time - (other?.time ?: 0)
    return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
}
