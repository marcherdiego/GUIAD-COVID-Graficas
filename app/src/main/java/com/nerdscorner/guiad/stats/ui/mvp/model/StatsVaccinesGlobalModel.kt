package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.events.coroutines.extensions.runAsync
import com.nerdscorner.events.coroutines.extensions.withResult
import com.nerdscorner.guiad.stats.domain.VaccinesData

class StatsVaccinesGlobalModel : StatsModel() {
    private var vaccinesData = VaccinesData.getInstance()

    override val availableStats by lazy { vaccinesData.getStats() }

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
                vaccinesData.getDataSet(stat, chartColor, chartColor)
            }
        }.await()
    }
}
