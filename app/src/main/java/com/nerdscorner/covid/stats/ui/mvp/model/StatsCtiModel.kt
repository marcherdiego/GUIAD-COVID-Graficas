package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CtiData

class StatsCtiModel : StatsModel() {
    private var ctiData = CtiData.getInstance()

    override val availableStats by lazy { ctiData.getStats() }
    
    override fun getDataSet(colorsList: List<Int>): List<ILineDataSet> {
        return selectedStats.map { stat ->
            val chartColor = colorsList[stat.index % colorsList.size]
            ctiData.getDataSet(stat, chartColor, chartColor)
        }
    }
}
