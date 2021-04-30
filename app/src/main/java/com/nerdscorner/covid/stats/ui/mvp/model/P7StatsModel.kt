package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData
import com.nerdscorner.covid.stats.domain.P7ByCityData
import com.nerdscorner.covid.stats.domain.P7Data

class P7StatsModel : StatsModel() {
    private var p7Data = P7Data.getInstance()
    private var p7ByCityData = P7ByCityData.getInstance()
    
    init {
        allCities = CitiesData.getAllCitiesNames()
    }

    override val availableStats by lazy {
        p7Data
            .getStats()
            .union(p7ByCityData.getStats())
            .toList()
    }

    override fun getDataSet(colorsList: List<Int>): List<ILineDataSet> {
        val selectedCities = listOf(allCities[selectedCity])
        return selectedStats.map { stat ->
            val chartColor = colorsList[stat.index % colorsList.size]
            if (stat in p7Data.getStats()) {
                p7Data.getDataSet(stat, chartColor, chartColor)
            } else {
                p7ByCityData.getDataSet(stat, selectedCities, chartColor, chartColor)
            }
        }
    }
}
