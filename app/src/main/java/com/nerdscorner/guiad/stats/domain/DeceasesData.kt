package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.extensions.getDaysDiff
import com.nerdscorner.guiad.stats.extensions.sortIf
import com.nerdscorner.guiad.stats.utils.DateUtils
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils
import java.util.*

class DeceasesData private constructor() : DataObject() {

    fun getDataSet(
        stat: Stat,
        selectedCities: List<String>,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int
    ): ILineDataSet {
        val selectedDataRange = getSelectedDataRange()
        val today = Date()
        val dataLines = dataLines
            .groupBy { it[stat.index] }
            .sortIf(stat.isSorted)
            .map { dataEntries ->
                val dataX = dataEntries.key
                val valuesSum = dataEntries
                    .value
                    .map { dataTokens ->
                        val date = DateUtils.parseDate(dataTokens[INDEX_DATE])
                        val city = dataTokens[INDEX_CITY]
                        if (today.getDaysDiff(date) <= selectedDataRange && city in selectedCities) {
                            1
                        } else {
                            0
                        }
                    }
                    .reduce { acc, s -> acc + s }
                return@map if (valuesSum == 0) {
                    null
                } else {
                    listOf(dataX, valuesSum.toString())
                }
            }
            .filterNotNull()
        return getDataSet(dataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor, dataLines.size)
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