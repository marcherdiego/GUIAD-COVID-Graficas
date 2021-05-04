package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.GeneralStatsData
import com.nerdscorner.covid.stats.utils.ColorUtils

class GeneralStatsModel : StatsModel() {
    private var generalStatsData = GeneralStatsData.getInstance()

    override val availableStats by lazy { generalStatsData.getStats() }

    override fun getDataSet(): List<ILineDataSet> {
        return selectedStats.map { stat ->
            val chartColor = ColorUtils.getColor(stat.index)
            generalStatsData.getDataSet(stat, chartColor, chartColor)
        }
    }
}
