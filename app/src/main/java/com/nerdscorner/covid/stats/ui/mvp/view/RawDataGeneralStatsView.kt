package com.nerdscorner.covid.stats.ui.mvp.view

import android.widget.TextView
import com.nerdscorner.covid.stats.R
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.covid.stats.ui.activities.RawDataGeneralStatsActivity
import com.nerdscorner.covid.stats.ui.custom.DatePicker
import java.util.*

class RawDataGeneralStatsView(activity: RawDataGeneralStatsActivity) : BaseActivityView(activity) {
    private val datePicker: DatePicker = activity.findViewById(R.id.date_picker)
    private val newCases: TextView = activity.findViewById(R.id.new_cases)
    
    init {
        datePicker.setDate(Date())
        datePicker.setDatePickedListener { 
            bus.post(DatePickedEvent(it))
        }
    }

    fun setNewCases(newCases: String) {
        this.newCases.text = newCases
    }
    
    class DatePickedEvent(val date: Date)
}
