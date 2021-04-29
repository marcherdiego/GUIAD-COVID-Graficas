package com.nerdscorner.covid.stats.ui.mvp.view

import android.widget.SpinnerAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.extensions.setItemSelectedListener
import com.nerdscorner.covid.stats.ui.activities.StatsByCityActivity

class StatsByCityView(activity: StatsByCityActivity) : StatsView(activity) {
    private val citiesSelector: AppCompatSpinner = activity.findViewById(R.id.cities_selector)
    private val statSelector: AppCompatSpinner = activity.findViewById(R.id.stat_selector)

    init {
        citiesSelector.setItemSelectedListener {
            bus.post(CitySelectedEvent(it))
        }
        statSelector.setItemSelectedListener {
            bus.post(StatSelectedEvent(it))
        }
    }

    fun setCitiesAdapter(adapter: SpinnerAdapter) {
        citiesSelector.adapter = adapter
    }

    fun setStatsAdapter(adapter: SpinnerAdapter) {
        statSelector.adapter = adapter
    }

    class CitySelectedEvent(val position: Int)
    class StatSelectedEvent(val position: Int)
}
