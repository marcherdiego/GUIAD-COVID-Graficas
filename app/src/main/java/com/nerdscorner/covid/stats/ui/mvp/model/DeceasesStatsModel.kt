package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.DeceasesData

class DeceasesStatsModel : StatsModel() {
    private var deceasesData = DeceasesData()

    init {
        allCities.clear()
        allCities.add("Artigas")
        allCities.add("Canelones")
        allCities.add("Cerro Largo")
        allCities.add("Colonia")
        allCities.add("Durazno")
        allCities.add("Flores")
        allCities.add("Florida")
        allCities.add("Lavalleja")
        allCities.add("Maldonado")
        allCities.add("Montevideo")
        allCities.add("Paysandú")
        allCities.add("Río Negro")
        allCities.add("Rivera")
        allCities.add("Rocha")
        allCities.add("Salto")
        allCities.add("San José")
        allCities.add("Soriano")
        allCities.add("Tacuarembó")
        allCities.add("Treinta y Tres")
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
