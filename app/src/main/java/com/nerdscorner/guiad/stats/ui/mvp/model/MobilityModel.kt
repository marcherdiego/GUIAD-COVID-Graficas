package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.domain.MobilityData
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.events.coroutines.extensions.runAsync
import com.nerdscorner.events.coroutines.extensions.withResult

class MobilityModel : StatsModel() {
    private var mobilityData = MobilityData.getInstance()

    override val availableStats by lazy { mobilityData.getStats() }

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
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                mobilityData.getLineDataSet(stat, chartColor, chartColor)
            }
        }.await()
    }

    override suspend fun createBarDataSets(): List<IBarDataSet> {
        return runAsync {
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                mobilityData.getBarDataSet(stat, chartColor, chartColor)
            }
        }.await()
    }
}
