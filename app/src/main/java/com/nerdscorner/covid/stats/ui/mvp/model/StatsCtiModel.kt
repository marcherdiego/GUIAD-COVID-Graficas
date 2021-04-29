package com.nerdscorner.covid.stats.ui.mvp.model

import android.app.Activity
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CtiData

class StatsCtiModel : StatsModel() {
    private lateinit var ctiData: CtiData

    override val dataFileName = CtiData.DATA_FILE_NAME
    override val availableStats by lazy { ctiData.getStats() }

    override fun loadChartsData(activity: Activity) {
        super.loadChartsData(activity)
        ctiData = CtiData(csvLines)
    }

    override fun getDataSet(colorsList: List<Int>): List<ILineDataSet> {
        return selectedStats.map { stat->
            val chartColor = colorsList[stat.index % colorsList.size]
            ctiData.getDataSet(stat, chartColor, chartColor)
        }
    }
}
