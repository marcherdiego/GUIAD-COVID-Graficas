package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.domain.GeneralStatsData
import com.nerdscorner.guiad.stats.domain.P7Data
import com.nerdscorner.guiad.stats.domain.Stat
import com.nerdscorner.guiad.stats.ui.custom.RawStat
import com.nerdscorner.events.coroutines.extensions.runAsync
import com.nerdscorner.events.coroutines.extensions.withResult
import com.nerdscorner.guiad.stats.domain.ChartType
import com.nerdscorner.guiad.stats.extensions.isSameDayOrAfter
import com.nerdscorner.guiad.stats.extensions.isSameDayOrBefore
import com.nerdscorner.guiad.stats.utils.*
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import java.text.SimpleDateFormat
import java.util.*

class RawDataGeneralStatsModel : BaseEventsModel() {

    private val generalStatsData = GeneralStatsData.getInstance()
    private val p7Data = P7Data.getInstance()

    var chartType: ChartType = SharedPreferencesUtils.getSelectedChartType()
        set(value) {
            field = value
            SharedPreferencesUtils.saveSelectedChartType(value)
        }
    val selectedStats = arrayListOf<Stat>()
    var currentDate = Date()
    var maxDateReached = true
    var minDateReached = false
    var selectedRange = SharedPreferencesUtils.getSelectedDataRangeIndex()
        set(value) {
            field = value
            SharedPreferencesUtils.saveSelectedDataRangeIndex(value)
        }

    fun getStatsForDate(): GeneralStatsData.StatsForDate {
        val dateFormat = SimpleDateFormat(DateUtils.DATE_FORMAT, Locale.getDefault())
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

    fun buildDataSets() {
        withResult(
            resultFunc = ::createLineDataSets,
            success = {
                bus.post(LineDataSetsBuiltEvent(this!!))
            }
        )
        withResult(
            resultFunc = ::createBarDataSets,
            success = {
                bus.post(BarDataSetsBuiltEvent(this!!))
            }
        )
    }

    private suspend fun createLineDataSets(): List<ILineDataSet> {
        return runAsync {
            val generalStatsDataSet = selectedStats
                .filter {
                    it != P7Data.p7Stat
                }
                .map { stat ->
                    val chartColor = ColorUtils.getColor(stat.index)
                    generalStatsData.getLineDataSet(stat, chartColor, chartColor)
                }
            val p7DataSet = selectedStats
                .filter {
                    it == P7Data.p7Stat
                }.map { stat ->
                    val chartColor = ColorUtils.getColor(generalStatsData.getStats().size + stat.index)
                    p7Data.getLineDataSet(stat, chartColor, chartColor)
                }
            generalStatsDataSet.union(p7DataSet).toList()
        }.await()
    }

    private suspend fun createBarDataSets(): List<IBarDataSet> {
        return runAsync {
            val generalStatsDataSet = selectedStats
                .filter {
                    it != P7Data.p7Stat
                }
                .map { stat ->
                    val chartColor = ColorUtils.getColor(stat.index)
                    generalStatsData.getBarDataSet(stat, chartColor, chartColor)
                }
            val p7DataSet = selectedStats
                .filter {
                    it == P7Data.p7Stat
                }.map { stat ->
                    val chartColor = ColorUtils.getColor(generalStatsData.getStats().size + stat.index)
                    p7Data.getBarDataSet(stat, chartColor, chartColor)
                }
            generalStatsDataSet.union(p7DataSet).toList()
        }.await()
    }

    fun getXValueForDate(): Float {
        val dateFormat = SimpleDateFormat(DateUtils.DATE_FORMAT, Locale.getDefault())
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

    class LineDataSetsBuiltEvent(val dataSets: List<ILineDataSet>)
    class BarDataSetsBuiltEvent(val dataSets: List<IBarDataSet>)

    companion object {
        val MIN_DATE: Date = GregorianCalendar(2020, 2, 13).time //March = 2
    }
}
