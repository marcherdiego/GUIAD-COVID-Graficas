package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.DateUtils
import com.nerdscorner.guiad.stats.utils.RangeUtils
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils
import java.text.SimpleDateFormat
import java.util.*

class P7Data private constructor() : DataObject() {

    private val cachedDatesIndexes = mutableListOf<String>()

    fun getDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int, limit: Int? = null): ILineDataSet {
        return getDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor, limit)
    }

    override fun setData(data: String?) {
        super.setData(data)
        dataLines.addAll(0, getMissingDummyData())
        cachedDatesIndexes.clear()
        dataLines.forEach {
            cachedDatesIndexes.add(it.split(COMMA)[INDEX_DATE])
        }
    }

    override fun getStats() = listOf(p7Stat)

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveP7Data(data)
    }

    fun getStatsForDate(filterDate: String): StatsForDate {
        val dataLine = dataLines.firstOrNull { it.split(COMMA)[INDEX_DATE] == filterDate }
        return if (dataLine == null) {
            StatsForDate()
        } else {
            val dataTokens = dataLine.split(COMMA)
            StatsForDate(p7 = dataTokens[INDEX_P7])
        }
    }

    fun indexOfDate(filterDate: String): Float {
        val selectedRangeIndex = SharedPreferencesUtils.getSelectedDataRangeIndex()
        val selectedDataRange = RangeUtils.getDaysCountForRangeIndex(selectedRangeIndex, dataLines.size)
        return cachedDatesIndexes.takeLast(selectedDataRange).indexOf(filterDate).toFloat()
    }

    private fun getMissingDummyData(): List<String> {
        val dateFormat = SimpleDateFormat(DateUtils.DATE_FORMAT, Locale.getDefault())
        val startDate = GregorianCalendar(2020, 2, 25)
        val endDate = GregorianCalendar(2020, 4, 6)
        val result = mutableListOf<String>()
        do {
            val filterDate = dateFormat.format(startDate.time)
            result.add(listOf(filterDate, 0).joinToString())
            startDate.add(Calendar.DATE, 1)
        } while (startDate.before(endDate))
        return result
    }

    data class StatsForDate(val p7: String = "N/A")

    companion object {
        private val instance = P7Data()

        fun getInstance() = instance

        private const val INDEX_DATE = 0
        private const val INDEX_P7 = 1

        val p7Stat = Stat("Índice p7/100.000 habitantes (País)", INDEX_P7)
    }
}