package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.extensions.getDaysDiff
import com.nerdscorner.guiad.stats.extensions.sortIf
import com.nerdscorner.guiad.stats.utils.DateUtils
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils
import java.util.*

class DeceasesData private constructor() : DataObject() {

    fun getLineDataSet(
        stat: Stat,
        selectedCities: List<String>,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        mode: LineDataSet.Mode? = null
    ): ILineDataSet {
        val selectedDataRange = getSelectedDataRange()
        val today = Date()
        val groupedLines = dataLines.groupBy { it[stat.index] }
        val sortedLines = groupedLines.sortIf(stat.isSorted)
        val dataLines = sortedLines.mapNotNull { dataEntries ->
            val dataX = dataEntries.key
            val valuesSum = dataEntries
                .value
                .map { dataTokens ->
                    val date = DateUtils.parseDate(dataTokens[INDEX_DATE])
                    val city = dataTokens[INDEX_CITY]
                    val daysDiff = today.getDaysDiff(date)
                    if (daysDiff in 0..selectedDataRange && city in selectedCities) {
                        1
                    } else {
                        0
                    }
                }
                .reduce { acc, s -> acc + s }
            return@mapNotNull if (valuesSum == 0) {
                null
            } else {
                listOf(dataX, valuesSum.toString())
            }
        }
        return getLineDataSet(dataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor, dataLines.size, mode)
    }

    fun getBarDataSet(
        stat: Stat,
        selectedCities: List<String>,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int
    ): IBarDataSet {
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
        return getBarDataSet(dataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor, dataLines.size)
    }

    override fun getStats() = listOf(dateStat, ageStat)

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveDeceasesData(data)
    }

    override fun getLatestUpdate() = getValueAt(dataLines.size - 1, dateStat) ?: N_A

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