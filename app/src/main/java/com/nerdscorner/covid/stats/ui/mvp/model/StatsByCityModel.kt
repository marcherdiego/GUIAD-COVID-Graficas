package com.nerdscorner.covid.stats.ui.mvp.model

import android.app.Activity
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData

class StatsByCityModel : StatsModel() {
    override val dataFileName = "data/estadisticasUY_porDepto_detalle.csv"

    private lateinit var citiesData: CitiesData
    
    var selectedCity = 0
    var selectedStat = 0
    
    val allCities = CitiesData.getAllCities()
    val availableStats = CitiesData.getStats()

    override fun loadChartsData(activity: Activity) {
        super.loadChartsData(activity)
        citiesData = CitiesData(csvLines)
    }

    fun getCityDataSets(colorsList: List<Int>): ILineDataSet {
        val selectedCities = if (selectedCity == 0) {
            allCities
        } else {
            listOf(allCities[selectedCity])
        }
        val selectedStat = availableStats[selectedStat]
        return citiesData.getDataSet(selectedStat.index, selectedCities, colorsList.first(), colorsList.first())
    }
}