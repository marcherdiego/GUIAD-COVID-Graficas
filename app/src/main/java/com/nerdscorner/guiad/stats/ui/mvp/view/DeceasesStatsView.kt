package com.nerdscorner.guiad.stats.ui.mvp.view

import android.widget.SpinnerAdapter
import com.nerdscorner.guiad.stats.extensions.setItemSelectedListener
import com.nerdscorner.guiad.stats.ui.activities.DeceasesStatsActivity

class DeceasesStatsView(activity: DeceasesStatsActivity) : StatsView(activity) {

    fun setStatsAdapter(adapter: SpinnerAdapter) {
        statSelector.adapter = adapter
        statSelector.setItemSelectedListener {
            bus.post(StatIndexSelectedEvent(it))
        }
    }
    
    class StatIndexSelectedEvent(val position: Int)
}
