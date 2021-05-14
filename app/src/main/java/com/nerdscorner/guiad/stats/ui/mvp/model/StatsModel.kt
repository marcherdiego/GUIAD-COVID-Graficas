package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.domain.ChartType
import com.nerdscorner.guiad.stats.domain.CitiesData
import com.nerdscorner.guiad.stats.domain.Stat
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils
import com.nerdscorner.mvplib.events.model.BaseEventsModel

abstract class StatsModel : BaseEventsModel() {

    var chartType: ChartType = SharedPreferencesUtils.getSelectedChartType()
        set(value) {
            field = value
            SharedPreferencesUtils.saveSelectedChartType(value)
        }
    var allCities = CitiesData.getAllCities()
    abstract val availableStats: List<Stat>

    // State vars
    var selectedCity = 0
    var selectedRange = SharedPreferencesUtils.getSelectedDataRangeIndex()
        set(value) {
            field = value
            SharedPreferencesUtils.saveSelectedDataRangeIndex(value)
        }
    var selectedStats = arrayListOf<Stat>()

    abstract fun buildDataSets()

    protected abstract suspend fun createLineDataSets(): List<ILineDataSet>

    protected abstract suspend fun createBarDataSets(): List<IBarDataSet>

    fun getStatsStateList(): List<Stat> {
        return availableStats
            .toMutableList()
            .apply {
                add(0, Stat())
            }
    }

    class LineDataSetsBuiltEvent(val dataSets: List<ILineDataSet>)
    class BarDataSetsBuiltEvent(val dataSets: List<IBarDataSet>)
}
