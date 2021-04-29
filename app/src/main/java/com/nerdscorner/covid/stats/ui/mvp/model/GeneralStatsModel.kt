package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.GeneralStatsData

class GeneralStatsModel : StatsModel() {
    private lateinit var generalStatsData: GeneralStatsData

    override val dataFileName = GeneralStatsData.DATA_FILE_NAME
    override val availableStats by lazy { generalStatsData.getStats() }

    override fun buildDataObject() {
        generalStatsData = GeneralStatsData(csvLines)
    }

    override fun getDataSet(colorsList: List<Int>): List<ILineDataSet> {
        return selectedStats.map { stat ->
            val chartColor = colorsList[stat.index % colorsList.size]
            generalStatsData.getDataSet(stat, chartColor, chartColor)
        }
    }
}
