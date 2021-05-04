package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData
import com.nerdscorner.covid.stats.utils.ColorUtils

class StatsByCityModel : StatsModel() {
    private var citiesData = CitiesData.getInstance()

    override val availableStats by lazy { citiesData.getStats() }

    override fun getDataSet(): List<ILineDataSet> {
        val selectedCities = if (selectedCity == 0) {
            allCities
        } else {
            listOf(allCities[selectedCity])
        }
        return selectedStats.map { stat ->
            val chartColor = ColorUtils.getColor(stat.index)
            citiesData.getDataSet(stat, selectedCities, chartColor, chartColor)
        }
    }
}