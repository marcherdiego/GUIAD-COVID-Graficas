package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.domain.GeneralStatsData
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.guiad.stats.extensions.runAsync

class GeneralStatsModel : StatsModel() {
    private var generalStatsData = GeneralStatsData.getInstance()

    override val availableStats by lazy { generalStatsData.getStats() }

    override suspend fun createLineDataSets(): List<ILineDataSet> {
        return runAsync {
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                generalStatsData.getLineDataSet(stat, chartColor, chartColor)
            }
        }.await()
    }

    override suspend fun createBarDataSets(): List<IBarDataSet> {
        return runAsync {
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                generalStatsData.getBarDataSet(stat, chartColor, chartColor)
            }
        }.await()
    }
}
