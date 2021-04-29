package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData

class StatsByCityModel : StatsModel() {
    private lateinit var citiesData: CitiesData

    override val dataFileName = CitiesData.DATA_FILE_NAME
    override val availableStats by lazy { citiesData.getStats() }

    override fun buildDataObject() {
        citiesData = CitiesData(csvLines)
    }

    override fun getDataSet(colorsList: List<Int>): List<ILineDataSet> {
        val selectedCities = if (selectedCity == 0) {
            allCities
        } else {
            listOf(allCities[selectedCity])
        }
        return selectedStats.map { stat ->
            val chartColor = colorsList[stat.index % colorsList.size]
            citiesData.getDataSet(stat, selectedCities, chartColor, chartColor)
        }
    }
}