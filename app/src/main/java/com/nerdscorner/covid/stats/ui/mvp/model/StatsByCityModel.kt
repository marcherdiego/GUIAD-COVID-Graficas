package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData
import com.nerdscorner.covid.stats.utils.ColorUtils
import com.nerdscorner.events.coroutines.extensions.runAsync
import com.nerdscorner.events.coroutines.extensions.withResult

class StatsByCityModel : StatsModel() {
    private var citiesData = CitiesData.getInstance()

    override val availableStats by lazy { citiesData.getStats() }

    override fun buildDataSets() {
        withResult(
            resultFunc = ::createDataSets,
            success = {
                bus.post(DataSetsBuiltEvent(this!!))
            }
        )
    }

    override suspend fun createDataSets(): List<ILineDataSet> {
        return runAsync {
            val selectedCities = if (selectedCity == 0) {
                allCities
            } else {
                listOf(allCities[selectedCity])
            }
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                citiesData.getDataSet(stat, selectedCities, chartColor, chartColor)
            }
        }.await()
    }
}