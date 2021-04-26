package com.nerdscorner.covid.stats.ui.mvp.view

import android.view.View
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.activities.StatsByCityActivity

class StatsByCityView(activity: StatsByCityActivity) : StatsView(activity) {
    private val citiesSelector: AppCompatSpinner = activity.findViewById(R.id.cities_selector)

    init {
        citiesSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                bus.post(CitySelectedEvent(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun setCitiesAdapter(adapter: SpinnerAdapter) {
        citiesSelector.adapter = adapter
    }

    class CitySelectedEvent(val position: Int)
}
