package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.utils.SharedPreferencesUtils

class DeceasesData private constructor() : DataObject() {

    fun getDataSet(
        stat: Stat,
        selectedCities: List<String>,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int? = null
    ): ILineDataSet {
        var dataMap = dataLines.groupBy { it.split(COMMA)[stat.index] }
        if (stat.isSorted) {
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
        return getDataSet(dataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor, limit)
    }

    override fun getStats() = listOf(dateStat, ageStat)

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveDeceasesData(data)
    }

    companion object {
        private val instance = DeceasesData()

        fun getInstance() = instance

        private const val INDEX_DATE = 0
        private const val INDEX_CITY = 1
        private const val INDEX_AGE = 2

        val dateStat = Stat("Por fecha", INDEX_DATE)
        val ageStat = Stat("Por edades", INDEX_AGE, isSorted = true)
    }
}