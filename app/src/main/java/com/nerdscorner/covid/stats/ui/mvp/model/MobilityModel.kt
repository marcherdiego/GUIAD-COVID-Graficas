package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.MobilityData
import com.nerdscorner.covid.stats.utils.ColorUtils

class MobilityModel : StatsModel() {
    private var mobilityData = MobilityData.getInstance()

    override val availableStats by lazy { mobilityData.getStats() }

    override fun getDataSet(): List<ILineDataSet> {
        return selectedStats.map { stat ->
            val chartColor = ColorUtils.getColor(stat.index)
            mobilityData.getDataSet(stat, chartColor, chartColor)
        }
    }
}
