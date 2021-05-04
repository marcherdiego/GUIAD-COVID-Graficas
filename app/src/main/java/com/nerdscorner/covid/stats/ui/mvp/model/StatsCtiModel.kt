package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CtiData
import com.nerdscorner.covid.stats.utils.ColorUtils

class StatsCtiModel : StatsModel() {
    private var ctiData = CtiData.getInstance()

    override val availableStats by lazy { ctiData.getStats() }

    override fun getDataSet(): List<ILineDataSet> {
        return selectedStats.map { stat ->
            val chartColor = ColorUtils.getColor(stat.index)
            ctiData.getDataSet(stat, chartColor, chartColor)
        }
    }
}
