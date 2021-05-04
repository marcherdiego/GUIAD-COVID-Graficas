package com.nerdscorner.covid.stats.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val US_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val UY_DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    fun formatDate(date: Date?): String {
        return if (date == null) {
            ""
        } else {
            UY_DATE_FORMAT.format(date)
        }
    }

    fun convertUsDateToUyDate(usDate: String): String {
        return try {
            UY_DATE_FORMAT.format(US_DATE_FORMAT.parse(usDate)!!)
        } catch (e: Exception) {
            usDate
        }
    }
}

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
