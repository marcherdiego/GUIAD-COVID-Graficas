package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.GeneralStatsData
import com.nerdscorner.covid.stats.domain.P7Data
import com.nerdscorner.covid.stats.domain.Stat
import com.nerdscorner.covid.stats.ui.custom.RawStat
import com.nerdscorner.covid.stats.utils.ColorUtils
import com.nerdscorner.covid.stats.utils.isSameDayOrAfter
import com.nerdscorner.covid.stats.utils.isSameDayOrBefore
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import java.text.SimpleDateFormat
import java.util.*

class RawDataGeneralStatsModel : BaseEventsModel() {

    private val generalStatsData = GeneralStatsData.getInstance()
    private val p7Data = P7Data.getInstance()

    val selectedStats = arrayListOf<Stat>()
    var currentDate = Date()
    var maxDateReached = true
    var minDateReached = false

    fun getStatsForDate(): GeneralStatsData.StatsForDate {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val filterDate = dateFormat.format(currentDate)
        val previousDayDate = getDateWithOffset(Calendar.DATE, -1)
        val filterPreviousDayDate = dateFormat.format(previousDayDate)
        return generalStatsData.getStatsForDate(filterDate, filterPreviousDayDate)
    }

    fun setDayOffset(offset: Int) {
        setDateFieldOffset(Calendar.DATE, offset)
    }

    fun setMonthOffset(offset: Int) {
        setDateFieldOffset(Calendar.MONTH, offset)
    }

    fun getDataSet(): List<ILineDataSet> {
        val generalStatsDataSet = selectedStats
            .filter {
                it != P7Data.p7Stat
            }
            .map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                generalStatsData.getDataSet(stat, chartColor, chartColor)
            }
        val p7DataSet = selectedStats
            .filter {
                it == P7Data.p7Stat
            }.map { stat ->
                val chartColor = ColorUtils.getColor(generalStatsData.getStats().size + stat.index)
                p7Data.getDataSet(stat, chartColor, chartColor)
            }
        return generalStatsDataSet.union(p7DataSet).toList()
    }

    fun getXValueForDate(): Float {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val filterDate = dateFormat.format(currentDate)
        return p7Data.indexOfDate(filterDate)
    }

    private fun getDateWithOffset(field: Int, offset: Int): Date {
        return Calendar.getInstance().apply {
            time = currentDate
            add(field, offset)
        }.time
    }

    private fun setDateFieldOffset(field: Int, offset: Int) {
        val newDate = getDateWithOffset(field, offset)
        updateCurrentDate(newDate)
    }

    fun updateSelectedStats(rawStat: RawStat) {
        val stat = rawStat.stat
        if (rawStat.itemSelected) {
            selectedStats.add(stat ?: return)
        } else {
            selectedStats.remove(stat)
        }
    }

    fun updateCurrentDate(stringDate: String) {
        val newDate = try {
            val dateTokens = stringDate.split("/")
            val year = dateTokens[2].toInt()
            val month = dateTokens[1].toInt() - 1
            val day = dateTokens[0].toInt()
            GregorianCalendar(year, month, day).time
        } catch (e: Exception) {
            Date()
        }
        updateCurrentDate(newDate)
    }

    fun updateCurrentDate(newDate: Date) {
        val today = Date()
        maxDateReached = false
        minDateReached = false
        currentDate = when {
            newDate.isSameDayOrAfter(today) -> {
                maxDateReached = true
                today
            }
            newDate.isSameDayOrBefore(MIN_DATE) -> {
                minDateReached = true
                MIN_DATE
            }
            else -> newDate
        }
    }

    fun updateSelectedStats(selectedStats: ArrayList<Stat>) {
        selectedStats.forEach { stat ->
            generalStatsData
                .getStats()
                .firstOrNull {
                    it.name == stat.name
                }
                ?.let {
                    this.selectedStats.add(it)
                }
        }
    }

    companion object {
        val MIN_DATE: Date = GregorianCalendar(2020, 2, 13).time //March = 2
    }
}
