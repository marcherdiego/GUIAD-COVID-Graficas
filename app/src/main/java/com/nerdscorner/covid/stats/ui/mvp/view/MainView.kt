package com.nerdscorner.covid.stats.ui.mvp.view

import android.widget.TextView
import com.nerdscorner.covid.stats.R
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.covid.stats.ui.activities.MainActivity

class MainView(activity: MainActivity) : BaseActivityView(activity) {

    private val lastUpdated: TextView = activity.findViewById(R.id.last_updated)

    init {
        onClick(R.id.cti_button, CtiButtonClickedEvent())
        onClick(R.id.cities_button, CitiesButtonClickedEvent())
        onClick(R.id.general_stats_button, GeneralStatsButtonClickedEvent())
        onClick(R.id.deceases_stats_button, DeceasesStatsButtonClickedEvent())
        onClick(R.id.p7_stats_button, P7StatsButtonClickedEvent())
        
        onClick(R.id.credits_button, CreditsButtonClickedEvent())
    }

    fun setLastUpdateDate(lastUpdateText: String) {
        lastUpdated.text = lastUpdateText
    }

    class CtiButtonClickedEvent
    class CitiesButtonClickedEvent
    class GeneralStatsButtonClickedEvent
    class DeceasesStatsButtonClickedEvent
    class P7StatsButtonClickedEvent
    class CreditsButtonClickedEvent
}
