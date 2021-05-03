package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.utils.SharedPreferencesUtils

class P7Data private constructor() : DataObject() {

    fun getDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        return getDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor)
    }

    override fun getStats() = listOf(p7Stat)

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveP7Data(data)
    }

    fun getStatsForDate(filterDate: String): StatsForDate {
        val dataLine = csvLines.firstOrNull { it.split(COMMA)[INDEX_DATE] == filterDate }
        return if (dataLine == null) {
            StatsForDate()
        } else {
            val dataTokens = dataLine.split(COMMA)
            StatsForDate(p7 = dataTokens[INDEX_P7])
        }
    }

    data class StatsForDate(
        val p7: String = "N/A"
    )

    companion object {
        private val instance = P7Data()

        fun getInstance() = instance

        private const val INDEX_DATE = 0
        private const val INDEX_P7 = 1

        val p7Stat = Stat("Índice p7/100.000 habitantes (País)", INDEX_P7)
    }
}