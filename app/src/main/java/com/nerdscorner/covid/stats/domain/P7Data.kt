package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class P7Data : DataObject(data) {

    fun getDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        return getDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor)
    }

    override fun getStats() = listOf(p7Stat)

    companion object {
        var data: String? = null

        private const val INDEX_DATE = 0
        private const val INDEX_P7 = 1

        val p7Stat = Stat("Índice p7/100.000 habitantes (País)", INDEX_P7)
    }
}