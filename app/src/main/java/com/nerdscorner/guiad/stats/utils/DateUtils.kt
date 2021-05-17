package com.nerdscorner.guiad.stats.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    const val DATE_FORMAT = "dd/MM/yyyy"
    const val DATE_TIME_FORMAT = "dd/MM/yyyy - HH:mm:ss"
    
    private val US_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")
    private val UY_DATE_FORMAT = SimpleDateFormat(DATE_FORMAT)

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
    
    fun parseDate(dateString: String): Date? = UY_DATE_FORMAT.parse(dateString)
}
