package com.nerdscorner.covid.stats.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val OUTPUT_DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    
    fun formatDate(date: Date?): String {
        return if (date == null) {
            ""
        } else {
            OUTPUT_DATE_FORMAT.format(date)
        }
    }
}