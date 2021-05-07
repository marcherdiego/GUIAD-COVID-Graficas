package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.domain.CitiesData
import com.nerdscorner.guiad.stats.domain.DeceasesData
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.events.coroutines.extensions.runAsync
import com.nerdscorner.events.coroutines.extensions.withResult

class DeceasesStatsModel : StatsModel() {
    private var deceasesData = DeceasesData.getInstance()

    init {
        allCities = CitiesData.getAllCitiesNamesIncludingAll()
    }

    override val availableStats by lazy { deceasesData.getStats() }

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
                deceasesData.getDataSet(stat, selectedCities, chartColor, chartColor)
            }
        }.await()
    }
}
