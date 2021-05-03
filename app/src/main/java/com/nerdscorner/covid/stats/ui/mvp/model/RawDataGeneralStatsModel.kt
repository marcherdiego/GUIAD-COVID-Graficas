package com.nerdscorner.covid.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.GeneralStatsData
import com.nerdscorner.covid.stats.domain.P7Data
import com.nerdscorner.covid.stats.domain.Stat
import com.nerdscorner.covid.stats.ui.custom.RawStat
import com.nerdscorner.covid.stats.utils.ColorUtils
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import java.text.SimpleDateFormat
import java.util.*

class RawDataGeneralStatsModel : BaseEventsModel() {

    private val generalStatsData = GeneralStatsData.getInstance()
    private val p7Data = P7Data.getInstance()
    private val selectedStats = mutableListOf<Stat>()

    var currentDate = Date()

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
        val colorsList = ColorUtils.graphColors
        val generalStatsDataSet = selectedStats
            .filter {
                it != P7Data.p7Stat
            }
            .map { stat ->
                val chartColor = colorsList[stat.index % colorsList.size]
                generalStatsData.getDataSet(stat, chartColor, chartColor)
            }
        val p7DataSet = selectedStats
            .filter {
                it == P7Data.p7Stat
            }.map { stat ->
                val chartColor = colorsList[(generalStatsData.getStats().size + stat.index) % colorsList.size]
                p7Data.getDataSet(stat, chartColor, chartColor)
            }
        return generalStatsDataSet.union(p7DataSet).toList()
    }

    private fun getDateWithOffset(field: Int, offset: Int): Date {
        return Calendar.getInstance().apply {
            time = currentDate
            add(field, offset)
        }.time
    }

    private fun setDateFieldOffset(field: Int, offset: Int) {
        val newDate = getDateWithOffset(field, offset)
        if (newDate.after(MIN_DATE) && newDate.before(Date())) {
            currentDate = newDate
        }
    }

    fun updateSelectedStats(rawStat: RawStat) {
        val stat = rawStat.stat
        if (rawStat.itemSelected) {
            selectedStats.add(stat ?: return)
        } else {
            selectedStats.remove(stat)
        }
    }

    companion object {
        val MIN_DATE: Date = GregorianCalendar(2020, 2, 13).time //March = 2
    }
}
