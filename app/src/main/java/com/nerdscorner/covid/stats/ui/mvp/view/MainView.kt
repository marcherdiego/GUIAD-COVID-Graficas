package com.nerdscorner.covid.stats.ui.mvp.view

import android.widget.ImageView
import android.widget.TextView
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.extensions.rotateCounterClockwise
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.covid.stats.ui.activities.MainActivity

class MainView(activity: MainActivity) : BaseActivityView(activity) {
    private val rotateDeviceIcon: ImageView? = activity.findViewById(R.id.rotate_device_icon)
    private val lastUpdated: TextView = activity.findViewById(R.id.last_updated)

    init {
        // Charts
        onClick(R.id.cti_button, CtiButtonClickedEvent())
        onClick(R.id.cities_button, CitiesButtonClickedEvent())
        onClick(R.id.general_stats_button, GeneralStatsButtonClickedEvent())
        onClick(R.id.deceases_stats_button, DeceasesStatsButtonClickedEvent())
        onClick(R.id.p7_stats_button, P7StatsButtonClickedEvent())
        onClick(R.id.mobility_stats_button, MobilityStatsButtonClickedEvent())

        // Raw data
        onClick(R.id.raw_data_general_stats_button, RawDataGeneralStatsButtonClickedEvent())

        onClick(R.id.credits_button, CreditsButtonClickedEvent())

        rotateDeviceIcon?.rotateCounterClockwise()
    }

    fun setLastUpdateDate(lastUpdateText: String) {
        lastUpdated.text = lastUpdateText
    }

    // Charts
    class CtiButtonClickedEvent
    class CitiesButtonClickedEvent
    class GeneralStatsButtonClickedEvent
    class DeceasesStatsButtonClickedEvent
    class P7StatsButtonClickedEvent
    class MobilityStatsButtonClickedEvent

    // Raw data
    class RawDataGeneralStatsButtonClickedEvent

    class CreditsButtonClickedEvent
}
