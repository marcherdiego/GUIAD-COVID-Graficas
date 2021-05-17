package com.nerdscorner.guiad.stats.ui.mvp.model

import androidx.annotation.CallSuper
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.domain.ChartType
import com.nerdscorner.guiad.stats.domain.CitiesData
import com.nerdscorner.guiad.stats.domain.Stat
import com.nerdscorner.guiad.stats.extensions.withResult
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.ThreadMode

abstract class StatsModel : BaseEventsModel() {

    var chartType: ChartType = SharedPreferencesUtils.getSelectedChartType()
        set(value) {
            field = value
            SharedPreferencesUtils.saveSelectedChartType(value)
        }
    var allCities = CitiesData.getAllCities()
    abstract val availableStats: List<Stat>
    private val pendingJobs = mutableListOf<Job>()

    // State vars
    var selectedCity = 0
    var selectedRange = SharedPreferencesUtils.getSelectedDataRangeIndex()
        set(value) {
            field = value
            SharedPreferencesUtils.saveSelectedDataRangeIndex(value)
        }
    var selectedStats = arrayListOf<Stat>()

    @CallSuper
    open fun buildDataSets() {
        cancelPendingJobs()
        withResult(
            resultFunc = {
                bus.post(LineDataSetsBuiltEvent(createLineDataSets()), ThreadMode.MAIN)
                bus.post(BarDataSetsBuiltEvent(createBarDataSets()), ThreadMode.MAIN)
            }
        ).enqueue()
    }

    protected abstract suspend fun createLineDataSets(): List<ILineDataSet>

    protected abstract suspend fun createBarDataSets(): List<IBarDataSet>

    fun getStatsStateList(): List<Stat> {
        return availableStats
            .toMutableList()
            .apply {
                add(0, Stat())
            }
    }
    
    protected fun cancelPendingJobs() {
        pendingJobs.forEach { 
            it.cancel()
        }
        pendingJobs.clear()
    }
    
    fun Job.enqueue() {
        pendingJobs.add(this)
    }

    class LineDataSetsBuiltEvent(val dataSets: List<ILineDataSet>)
    class BarDataSetsBuiltEvent(val dataSets: List<IBarDataSet>)
}
