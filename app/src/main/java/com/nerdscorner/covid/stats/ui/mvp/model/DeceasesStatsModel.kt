package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData
import com.nerdscorner.covid.stats.domain.DeceasesData

class DeceasesStatsModel : StatsModel() {
    private var deceasesData = DeceasesData.getInstance()

    init {
        allCities = CitiesData.getAllCitiesNames()
    }

    override val availableStats by lazy { deceasesData.getStats() }

    override fun getDataSet(colorsList: List<Int>): List<ILineDataSet> {
        val selectedCities = if (selectedCity == 0) {
            allCities
        } else {
            listOf(allCities[selectedCity])
        }
        return selectedStats.map { stat ->
            val chartColor = colorsList[stat.index % colorsList.size]
            deceasesData.getDataSet(stat, selectedCities, chartColor, chartColor)
        }
    }
}
