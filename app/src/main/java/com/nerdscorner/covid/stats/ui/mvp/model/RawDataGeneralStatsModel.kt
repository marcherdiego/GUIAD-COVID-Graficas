package com.nerdscorner.covid.stats.ui.mvp.model

import com.nerdscorner.covid.stats.domain.GeneralStatsData
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import java.text.SimpleDateFormat
import java.util.*

class RawDataGeneralStatsModel : BaseEventsModel() {

    private val generalStatsData = GeneralStatsData.getInstance()

    var currentDate = Date()

    fun getStatsForDate(): GeneralStatsData.StatsForDate {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val filterDate = dateFormat.format(currentDate)
        val previousDayDate = getDateWithOffset(Calendar.DATE, -1)
        val filterPreviousDayDate = dateFormat.format(previousDayDate)
        return generalStatsData.getStatsForDate(filterDate, filterPreviousDayDate)
    }

    fun setDayOffset(offset: Int) {
        setDateFieldOffset(Calendar.DATE, offset)
    }

    fun setMonthOffset(offset: Int) {
        setDateFieldOffset(Calendar.MONTH, offset)
    }

    private fun getDateWithOffset(field: Int, offset: Int): Date {
        return Calendar.getInstance().apply {
            time = currentDate
            add(field, offset)
        }.time
    }

    private fun setDateFieldOffset(field: Int, offset: Int) {
        val newDate = getDateWithOffset(field, offset)
        if (newDate.after(MIN_DATE) && newDate.before(Date())) {
            currentDate = newDate
        }
    }

    companion object {
        val MIN_DATE: Date = GregorianCalendar(2020, 2, 13).time //March = 2
    }
}
