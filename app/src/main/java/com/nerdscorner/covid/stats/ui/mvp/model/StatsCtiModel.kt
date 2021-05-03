package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CtiData
import com.nerdscorner.covid.stats.utils.ColorUtils

class StatsCtiModel : StatsModel() {
    private var ctiData = CtiData.getInstance()

    override val availableStats by lazy { ctiData.getStats() }

    override fun getDataSet(): List<ILineDataSet> {
        val colorsList = ColorUtils.graphColors
        return selectedStats.map { stat ->
            val chartColor = colorsList[stat.index % colorsList.size]
            ctiData.getDataSet(stat, chartColor, chartColor)
        }
    }
}
