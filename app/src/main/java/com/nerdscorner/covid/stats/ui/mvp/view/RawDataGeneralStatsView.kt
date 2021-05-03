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

    init {
        datePicker.setDatePickedListener {
            bus.post(DatePickedEvent(it))
        }
    }

    fun setMinDate(date: Date?) {
        datePicker.setMinDate(date)
    }

    fun setDate(date: Date) {
        datePicker.setDate(date)
    }

    fun setNewCases(newCases: String) {
        this.newCases.setValue(newCases)
    }

    class DatePickedEvent(val date: Date)
}
