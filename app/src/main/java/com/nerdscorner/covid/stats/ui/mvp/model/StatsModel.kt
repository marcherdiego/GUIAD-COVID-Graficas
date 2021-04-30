package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData
import com.nerdscorner.covid.stats.domain.Stat
import com.nerdscorner.mvplib.events.model.BaseEventsModel

abstract class StatsModel : BaseEventsModel() {

    val allCities = CitiesData.getAllCities().toMutableList()
    abstract val availableStats: List<Stat>

    // State vars
    var selectedCity = 0
    var selectedStats = listOf<Stat>()

    abstract fun getDataSet(colorsList: List<Int>): List<ILineDataSet>

    fun getStatsStateList(): List<Stat> {
        return availableStats
            .toMutableList()
            .apply {
                add(0, Stat("Seleccionar"))
            }
    }

    fun setSelectedStatIndex(index: Int) {
        selectedStats = listOf(availableStats[index])
    }
}
