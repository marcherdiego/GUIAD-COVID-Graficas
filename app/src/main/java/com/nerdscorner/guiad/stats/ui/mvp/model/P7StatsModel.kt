package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.domain.CitiesData
import com.nerdscorner.guiad.stats.domain.P7ByCityData
import com.nerdscorner.guiad.stats.domain.P7Data
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.events.coroutines.extensions.runAsync
import com.nerdscorner.events.coroutines.extensions.withResult

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
                if (stat in p7Data.getStats()) {
                    p7Data.getDataSet(stat, chartColor, chartColor)
                } else {
                    p7ByCityData.getDataSet(stat, selectedCities, chartColor, chartColor)
                }
            }
        }.await()
    }
}
