package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.nerdscorner.guiad.stats.domain.CtiData
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.events.coroutines.extensions.runAsync
import com.nerdscorner.events.coroutines.extensions.withResult

class StatsCtiModel : StatsModel() {
    private var ctiData = CtiData.getInstance()

    override val availableStats by lazy { ctiData.getStats() }

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
                ctiData.getLineDataSet(stat, chartColor, chartColor)
            }
        }.await()
    }

    override suspend fun createBarDataSets(): List<IBarDataSet> {
        return runAsync {
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                ctiData.getBarDataSet(stat, chartColor, chartColor)
            }
        }.await()
    }
}
