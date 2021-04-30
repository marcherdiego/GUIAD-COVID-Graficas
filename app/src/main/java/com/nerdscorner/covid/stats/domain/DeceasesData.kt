package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class DeceasesData : DataObject(data) {

    fun getDataSet(stat: Stat, selectedCities: List<String>, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        var dataMap = csvLines
            .asSequence()
            .drop(1) //Drop header
            .groupBy {
                it.split(COMMA)[stat.index]
            }
        if (stat == ageStat) {
            dataMap = dataMap.toSortedMap()
        }
        val dataLines = dataMap.map { dateEntries ->
                val date = dateEntries.key
                val valuesSum = dateEntries
                    .value
                    .map {
                        val dataTokens = it.split(COMMA)
                        val city = dataTokens[INDEX_CITY]
                        if (city in selectedCities) {
                            1
                        } else {
                            0
                        }
                    }
                    .reduce { acc, s -> acc + s }
                return@map "$date,$valuesSum"
            }
        return getDataSet(dataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor)
    }

    override fun getStats() = listOf(dateStat, ageStat)

    companion object {
        var data: String? = null

        private const val INDEX_DATE = 0
        private const val INDEX_CITY = 1
        private const val INDEX_AGE = 2

        private val dateStat = Stat("Por fecha", INDEX_DATE)
        private val ageStat = Stat("Por edades", INDEX_AGE)
    }
}