package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData
import com.nerdscorner.covid.stats.domain.Stat
import com.nerdscorner.covid.stats.utils.SharedPreferencesUtils
import com.nerdscorner.mvplib.events.model.BaseEventsModel

abstract class StatsModel : BaseEventsModel() {

    var allCities = CitiesData.getAllCities()
    abstract val availableStats: List<Stat>

    // State vars
    var selectedCity = 0
    var selectedRange = SharedPreferencesUtils.getSelectedDataRangeIndex()
        set(value) {
            field = value
            SharedPreferencesUtils.saveSelectedDataRangeIndex(value)
        }
    var selectedStats = arrayListOf<Stat>()

    abstract fun getDataSet(): List<ILineDataSet>

    fun getStatsStateList(): List<Stat> {
        return availableStats
            .toMutableList()
            .apply {
                add(0, Stat())
            }
    }
}
