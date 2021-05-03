package com.nerdscorner.covid.stats.ui.mvp.view

import com.nerdscorner.covid.stats.R
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.covid.stats.ui.activities.RawDataGeneralStatsActivity
import com.nerdscorner.covid.stats.ui.custom.DatePicker
import com.nerdscorner.covid.stats.ui.custom.RawStat
import java.util.*

class RawDataGeneralStatsView(activity: RawDataGeneralStatsActivity) : BaseActivityView(activity) {
    private val datePicker: DatePicker = activity.findViewById(R.id.date_picker)
    private val newCases: RawStat = activity.findViewById(R.id.new_cases)
    private val totalCases: RawStat = activity.findViewById(R.id.total_cases)
    private val ctiCases: RawStat = activity.findViewById(R.id.cti_cases)
    private val activeCases: RawStat = activity.findViewById(R.id.active_cases)
    private val recoveredCases: RawStat = activity.findViewById(R.id.recovered_cases)
    private val totalRecovered: RawStat = activity.findViewById(R.id.total_Recovered)
    private val deceases: RawStat = activity.findViewById(R.id.deceases)
    private val totalDeceases: RawStat = activity.findViewById(R.id.total_deceases)
    private val newTests: RawStat = activity.findViewById(R.id.new_tests)
    private val positivity: RawStat = activity.findViewById(R.id.positivity)
    private val harvardIndex: RawStat = activity.findViewById(R.id.harvard_index)
    private val indexVariation: RawStat = activity.findViewById(R.id.index_variation)

    init {
        datePicker.setDatePickedListener {
            bus.post(DatePickedEvent(it))
        }
        onClick(R.id.back_day_button, BackDayButtonClickedEvent())
        onClick(R.id.back_month_button, BackMonthButtonClickedEvent())
        onClick(R.id.forward_day_button, ForwardDayButtonClickedEvent())
        onClick(R.id.forward_month_button, ForwardMonthButtonClickedEvent())
    }

    fun setMinDate(date: Date?) {
        datePicker.setMinDate(date)
    }

    fun setDate(date: Date) {
        datePicker.setDate(date, manualUpdate = true)
    }

    fun setNewCases(
        newCases: String,
        totalCases: String,
        ctiCases: String,
        activeCases: String,
        recoveredCases: String,
        totalRecovered: String,
        deceases: String,
        totalDeceases: String,
        newTests: String,
        positivity: String,
        harvardIndex: String,
        indexVariation: String
    ) {
        this.newCases.setValue(newCases)
        this.totalCases.setValue(totalCases)
        this.ctiCases.setValue(ctiCases)
        this.activeCases.setValue(activeCases)
        this.recoveredCases.setValue(recoveredCases)
        this.totalRecovered.setValue(totalRecovered)
        this.deceases.setValue(deceases)
        this.totalDeceases.setValue(totalDeceases)
        this.newTests.setValue(newTests)
        this.positivity.setValue(positivity)
        this.harvardIndex.setValue(harvardIndex)
        this.indexVariation.setValue(indexVariation)
    }

    class DatePickedEvent(val date: Date)
    
    class BackDayButtonClickedEvent
    class BackMonthButtonClickedEvent
    class ForwardDayButtonClickedEvent
    class ForwardMonthButtonClickedEvent
}
