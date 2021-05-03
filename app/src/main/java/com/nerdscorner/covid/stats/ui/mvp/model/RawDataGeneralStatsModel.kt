package com.nerdscorner.covid.stats.ui.mvp.model

import com.nerdscorner.covid.stats.domain.GeneralStatsData
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import java.text.SimpleDateFormat
import java.util.*

class RawDataGeneralStatsModel : BaseEventsModel() {

    private val generalStatsData = GeneralStatsData.getInstance()

    fun getNewCases(date: Date): String {
        val filterDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        return generalStatsData.getNewCasesForDate(filterDate)
    }

    companion object {
        val MIN_DATE: Date = GregorianCalendar(2020, 2, 13).time //March = 2
    }
}
