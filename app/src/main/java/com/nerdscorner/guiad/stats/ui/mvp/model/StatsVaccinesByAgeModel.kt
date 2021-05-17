package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.guiad.stats.extensions.runAsync
import com.nerdscorner.guiad.stats.extensions.withResult
import com.nerdscorner.guiad.stats.domain.VaccinesByAgeData
import org.greenrobot.eventbus.ThreadMode

class StatsVaccinesByAgeModel : StatsModel() {
    private var statVaccineByAge = VaccinesByAgeData.getInstance()

    override val availableStats by lazy { statVaccineByAge.getStats() }

    override suspend fun createLineDataSets(): List<ILineDataSet> {
        return runAsync {
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                statVaccineByAge.getLineDataSet(stat, chartColor, chartColor)
            }
        }.await()
    }

    override suspend fun createBarDataSets(): List<IBarDataSet> {
        return runAsync {
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                statVaccineByAge.getBarDataSet(stat, chartColor, chartColor)
            }
        }.await()
    }
}
