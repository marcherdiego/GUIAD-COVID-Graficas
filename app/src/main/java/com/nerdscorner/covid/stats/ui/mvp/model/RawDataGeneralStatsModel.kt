package com.nerdscorner.covid.stats.ui.mvp.model

import com.nerdscorner.covid.stats.domain.GeneralStatsData
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import java.util.*

class RawDataGeneralStatsModel : BaseEventsModel() {
    
    private val generalStatsData = GeneralStatsData.getInstance()
    
    fun getNewCases(date: Date) = generalStatsData.getNewCasesForDate(date)
}
