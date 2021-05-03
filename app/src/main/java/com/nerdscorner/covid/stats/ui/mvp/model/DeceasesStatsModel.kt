package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData
import com.nerdscorner.covid.stats.domain.DeceasesData
import com.nerdscorner.covid.stats.utils.ColorUtils

class DeceasesStatsModel : StatsModel() {
    private var deceasesData = DeceasesData.getInstance()

    init {
        allCities = CitiesData.getAllCitiesNamesIncludingAll()
    }

    override val availableStats by lazy { deceasesData.getStats() }

    override fun getDataSet(): List<ILineDataSet> {
        val colorsList = ColorUtils.graphColors
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
