package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.GeneralStatsData

class GeneralStatsModel : StatsModel() {
    private var generalStatsData = GeneralStatsData.getInstance()

    override val availableStats by lazy { generalStatsData.getStats() }

    override fun getDataSet(colorsList: List<Int>): List<ILineDataSet> {
        return selectedStats.map { stat ->
            val chartColor = colorsList[stat.index % colorsList.size]
            generalStatsData.getDataSet(stat, chartColor, chartColor)
        }
    }
}
