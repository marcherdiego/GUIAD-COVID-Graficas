package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.GeneralStatsData
import com.nerdscorner.covid.stats.utils.ColorUtils
import com.nerdscorner.events.coroutines.extensions.runAsync
import com.nerdscorner.events.coroutines.extensions.withResult

class GeneralStatsModel : StatsModel() {
    private var generalStatsData = GeneralStatsData.getInstance()

    override val availableStats by lazy { generalStatsData.getStats() }

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
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                generalStatsData.getDataSet(stat, chartColor, chartColor)
            }
        }.await()
    }
}
