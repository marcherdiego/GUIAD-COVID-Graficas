package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.events.coroutines.extensions.runAsync
import com.nerdscorner.events.coroutines.extensions.withResult
import com.nerdscorner.guiad.stats.domain.CitiesData
import com.nerdscorner.guiad.stats.domain.VaccinesByCityData

class StatsVaccinesByCityModel : StatsModel() {
    private var vaccinesByCityData = VaccinesByCityData.getInstance()

    override val availableStats by lazy { vaccinesByCityData.getStats() }

    init {
        allCities = CitiesData.getAllCitiesNames()
    }

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
            val selectedCity = allCities[selectedCity]
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                vaccinesByCityData.getLineDataSet(stat, selectedCity, chartColor, chartColor)
            }
        }.await()
    }

    override suspend fun createBarDataSets(): List<IBarDataSet> {
        return runAsync {
            val selectedCity = allCities[selectedCity]
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                vaccinesByCityData.getBarDataSet(stat, selectedCity, chartColor, chartColor)
            }
        }.await()
    }
}
