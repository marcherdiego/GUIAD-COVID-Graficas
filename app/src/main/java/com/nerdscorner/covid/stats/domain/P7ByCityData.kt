package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class P7ByCityData : DataObject(data) {

    fun getDataSet(stat: Stat, selectedCities: List<String>, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        val dataLines = csvLines
            .asSequence()
            .drop(1) //Drop header
            .groupBy {
                it.split(COMMA)[INDEX_DATE]
            }
            .map { dateEntries ->
                val date = dateEntries.key
                val valuesSum = dateEntries
                    .value
                    .map {
                        val dataTokens = it.split(COMMA)
                        val city = dataTokens[INDEX_CITY]
                        if (city in selectedCities) {
                            dataTokens[stat.index].toFloat()
                        } else {
                            0f
                        }
                    }
                    .sum()
                return@map "$date,$valuesSum"
            }
        return getDataSet(dataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor)
    }

    override fun getStats() = listOf(p7Stat, p7Per100KStat)

    companion object {
        var data: String? = null

        private const val INDEX_DATE = 0
        private const val INDEX_CITY = 1
        private const val INDEX_ISO = 2
        private const val INDEX_ADMLCD = 3
        private const val INDEX_POPULATION = 4
        private const val INDEX_P7 = 5
        private const val INDEX_P7_PER_100K = 6

        val p7Stat = Stat("Índice p7 (por departamento)", INDEX_P7)
        val p7Per100KStat = Stat("Índice p7/100.000 habitantes (por departamento)", INDEX_P7_PER_100K)
    }
}