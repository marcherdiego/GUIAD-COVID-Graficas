package com.nerdscorner.covid.stats.ui.mvp.view

import android.widget.SpinnerAdapter
import com.nerdscorner.covid.stats.extensions.setItemSelectedListener
import com.nerdscorner.covid.stats.ui.activities.DeceasesStatsActivity

class DeceasesStatsView(activity: DeceasesStatsActivity) : StatsView(activity) {

    fun setStatsAdapter(adapter: SpinnerAdapter) {
        statSelector.adapter = adapter
        statSelector.setItemSelectedListener {
            bus.post(StatIndexSelectedEvent(it))
        }
    }
    
    class StatIndexSelectedEvent(val position: Int)
}
