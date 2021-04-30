package com.nerdscorner.covid.stats.ui.mvp.view

import com.nerdscorner.covid.stats.R
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.covid.stats.ui.activities.MainActivity

class MainView(activity: MainActivity) : BaseActivityView(activity) {

    init {
        onClick(R.id.cti_button, CtiButtonClickedEvent())
        onClick(R.id.cities_button, CitiesButtonClickedEvent())
        onClick(R.id.general_stats_button, GeneralStatsButtonClickedEvent())
        onClick(R.id.deceases_stats_button, DeceasesStatsButtonClickedEvent())
    }

    class CtiButtonClickedEvent
    class CitiesButtonClickedEvent
    class GeneralStatsButtonClickedEvent
    class DeceasesStatsButtonClickedEvent
}
