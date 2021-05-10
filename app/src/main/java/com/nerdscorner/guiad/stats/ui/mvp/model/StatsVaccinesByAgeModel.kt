package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.events.coroutines.extensions.runAsync
import com.nerdscorner.events.coroutines.extensions.withResult
import com.nerdscorner.guiad.stats.domain.VaccinesByAgeData
import com.nerdscorner.guiad.stats.ui.activities.StatsVaccineByAgeActivity

class StatsVaccinesByAgeModel : StatsModel() {
    private var statVaccineByAge = VaccinesByAgeData.getInstance()

    override val availableStats by lazy { statVaccineByAge.getStats() }

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
                statVaccineByAge.getDataSet(stat, chartColor, chartColor)
            }
        }.await()
    }
}
