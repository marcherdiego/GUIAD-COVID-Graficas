package com.nerdscorner.covid.stats.ui.mvp.model

import android.app.Activity
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData

class StatsByCityModel : StatsModel() {
    override val dataFileName = "data/estadisticasUY_porDepto_detalle.csv"
    
    private lateinit var citiesData: CitiesData

    override fun loadChartsData(activity: Activity) {
        super.loadChartsData(activity)
        citiesData = CitiesData(csvLines)
    }

    fun getCitiesDataSets(cities: List<String>, colorsList: List<Int>): List<ILineDataSet> {
        return CitiesData
            .getDataIndexes()
            .mapIndexed { index, dataIndex ->
                val colorIndex = index % colorsList.size
                citiesData.getDataSet(dataIndex, cities, colorsList[colorIndex], colorsList[colorIndex])
            }
    }

    fun getCities(): List<String> {
        return CitiesData.getAllCities()
    }

}
