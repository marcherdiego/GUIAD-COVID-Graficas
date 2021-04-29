package com.nerdscorner.covid.stats.ui.mvp.model

import android.app.Activity
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData

class StatsByCityModel : StatsModel() {
    private lateinit var citiesData: CitiesData

    override val dataFileName = CitiesData.DATA_FILE_NAME
    override val availableStats = citiesData.getStats()

    override fun loadChartsData(activity: Activity) {
        super.loadChartsData(activity)
        citiesData = CitiesData(csvLines)
    }

    override fun getDataSet(colorsList: List<Int>): List<ILineDataSet> {
        val selectedCities = if (selectedCity == 0) {
            allCities
        } else {
            listOf(allCities[selectedCity])
        }
        
        return selectedStats.map {
            val chartColor = colorsList[it.index % colorsList.size]
            citiesData.getDataSet(it.index, selectedCities, chartColor, chartColor)
        }
    }
}