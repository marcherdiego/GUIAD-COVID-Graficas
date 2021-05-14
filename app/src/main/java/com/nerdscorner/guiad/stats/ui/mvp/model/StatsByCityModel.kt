package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.domain.CitiesData
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.events.coroutines.extensions.runAsync
import com.nerdscorner.events.coroutines.extensions.withResult

class StatsByCityModel : StatsModel() {
    private var citiesData = CitiesData.getInstance()

    override val availableStats by lazy { citiesData.getStats() }

    override fun buildDataSets() {
        withResult(
            resultFunc = ::createLineDataSets,
            success = {
                bus.post(LineDataSetsBuiltEvent(this!!))
            }
        )
        withResult(
            resultFunc = ::createBarDataSets,
            success = {
                bus.post(BarDataSetsBuiltEvent(this!!))
            }
        )
    }

    override suspend fun createLineDataSets(): List<ILineDataSet> {
        return runAsync {
            val selectedCities = if (selectedCity == 0) {
                allCities
            } else {
                listOf(allCities[selectedCity])
            }
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                citiesData.getLineDataSet(stat, selectedCities, chartColor, chartColor)
            }
        }.await()
    }

    override suspend fun createBarDataSets(): List<IBarDataSet> {
        return runAsync {
            val selectedCities = if (selectedCity == 0) {
                allCities
            } else {
                listOf(allCities[selectedCity])
            }
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                citiesData.getBarDataSet(stat, selectedCities, chartColor, chartColor)
            }
        }.await()
    }
}