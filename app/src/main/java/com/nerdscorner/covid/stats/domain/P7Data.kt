package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.utils.SharedPreferencesUtils
import java.text.SimpleDateFormat
import java.util.*

class P7Data private constructor() : DataObject() {

    val cachedDatesIndexes = mutableListOf<String>()

    fun getDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        return getDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor)
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
        val dataLine = csvLines.firstOrNull { it.split(COMMA)[INDEX_DATE] == filterDate }
        return if (dataLine == null) {
            StatsForDate()
        } else {
            val dataTokens = dataLine.split(COMMA)
            StatsForDate(p7 = dataTokens[INDEX_P7])
        }
    }

    fun indexOfDate(filterDate: String) = cachedDatesIndexes.indexOf(filterDate).toFloat()

    private fun getMissingDummyData(): List<String> {
        val startDate = GregorianCalendar(2020, 2, 25)
        val endDate = GregorianCalendar(2020, 4, 6)
        val result = mutableListOf<String>()
        do {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val filterDate = dateFormat.format(startDate.time)
            result.add("$filterDate,0")
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