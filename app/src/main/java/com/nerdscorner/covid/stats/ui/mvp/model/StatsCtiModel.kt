package com.nerdscorner.covid.stats.ui.mvp.model

import android.app.Activity
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CtiData

class StatsCtiModel : StatsModel() {
    override val dataFileName = "data/estadisticasUY_cti.csv"
    
    private lateinit var ctiData: CtiData

    override fun loadChartsData(activity: Activity) {
        super.loadChartsData(activity)
        ctiData = CtiData(csvLines)
    }

    fun getAllDataSets(colorsList: List<Int>): List<ILineDataSet> {
        return CtiData
            .getDataIndexes()
            .mapIndexed { index, dataIndex ->
                val colorIndex = index % colorsList.size
                ctiData.getDataSet(dataIndex, colorsList[colorIndex], colorsList[colorIndex])
            }
    }
}
