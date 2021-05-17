package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.domain.CitiesData
import com.nerdscorner.guiad.stats.domain.P7ByCityData
import com.nerdscorner.guiad.stats.domain.P7Data
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.guiad.stats.extensions.runAsync
import com.nerdscorner.guiad.stats.extensions.withResult
import org.greenrobot.eventbus.ThreadMode

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

    override suspend fun createLineDataSets(): List<ILineDataSet> {
        return runAsync {
            val selectedCities = if (selectedCity == 0) {
                allCities
            } else {
                listOf(allCities[selectedCity])
            }
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                if (stat in p7Data.getStats()) {
                    p7Data.getLineDataSet(stat, chartColor, chartColor)
                } else {
                    p7ByCityData.getLineDataSet(stat, selectedCities, chartColor, chartColor)
                }
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
                if (stat in p7Data.getStats()) {
                    p7Data.getBarDataSet(stat, chartColor, chartColor)
                } else {
                    p7ByCityData.getBarDataSet(stat, selectedCities, chartColor, chartColor)
                }
            }
        }.await()
    }
}
