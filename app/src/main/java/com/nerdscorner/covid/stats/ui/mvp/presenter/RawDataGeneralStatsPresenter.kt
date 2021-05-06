package com.nerdscorner.covid.stats.ui.mvp.presenter

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.domain.GeneralStatsData
import com.nerdscorner.covid.stats.domain.P7Data
import com.nerdscorner.covid.stats.domain.Stat
import com.nerdscorner.covid.stats.extensions.formatNumberString
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter

import com.nerdscorner.covid.stats.ui.mvp.model.RawDataGeneralStatsModel
import com.nerdscorner.covid.stats.ui.mvp.view.RawDataGeneralStatsView
import com.nerdscorner.covid.stats.utils.RangeUtils
import org.greenrobot.eventbus.Subscribe
import java.util.*
import kotlin.collections.ArrayList

class RawDataGeneralStatsPresenter(view: RawDataGeneralStatsView, model: RawDataGeneralStatsModel) :
    BaseActivityPresenter<RawDataGeneralStatsView, RawDataGeneralStatsModel>(view, model) {

    init {
        view.getStats(
            newCases = GeneralStatsData.newCasesAdjustedStat,
            totalCases = GeneralStatsData.totalCasesStat,
            ctiCases = GeneralStatsData.totalCtiStat,
            activeCases = GeneralStatsData.inCourseStat,
            recoveredCases = GeneralStatsData.newRecoveredStat,
            totalRecovered = GeneralStatsData.totalRecoveredStat,
            deceases = GeneralStatsData.newDeceasesStat,
            totalDeceases = GeneralStatsData.totalDeceasesStat,
            newTests = GeneralStatsData.newTestsStat,
            positivity = GeneralStatsData.positivityStat,
            harvardIndex = P7Data.p7Stat
        )
        view.setMinDate(RawDataGeneralStatsModel.MIN_DATE)
    }

    @Subscribe
    fun onDatePicked(event: RawDataGeneralStatsView.DatePickedEvent) {
        model.currentDate = event.date
        refreshDateStats()
        refreshDateSeekButtons()
    }

    @Subscribe
    fun onBackDayButtonClicked(event: RawDataGeneralStatsView.BackDayButtonClickedEvent) {
        model.setDayOffset(-1)
        refreshDateStats()
        refreshDateSeekButtons()
    }

    @Subscribe
    fun onBackMonthButtonClicked(event: RawDataGeneralStatsView.BackMonthButtonClickedEvent) {
        model.setMonthOffset(-1)
        refreshDateStats()
        refreshDateSeekButtons()
    }

    @Subscribe
    fun onForwardDayButtonClicked(event: RawDataGeneralStatsView.ForwardDayButtonClickedEvent) {
        model.setDayOffset(1)
        refreshDateStats()
        refreshDateSeekButtons()
    }

    @Subscribe
    fun onForwardMonthButtonClicked(event: RawDataGeneralStatsView.ForwardMonthButtonClickedEvent) {
        model.setMonthOffset(1)
        refreshDateStats()
        refreshDateSeekButtons()
    }

    @Subscribe
    fun onStatClicked(event: RawDataGeneralStatsView.StatClickedEvent) {
        model.updateSelectedStats(event.rawStat)
        model.buildDataSets()
        view.setChartSelectedItem(model.getXValueForDate())
    }

    @Subscribe
    fun onChartValueSelected(event: RawDataGeneralStatsView.ChartValueSelectedEvent) {
        val selectedDate = (event.entry?.data as? String)?.split(" - ")?.get(0) ?: return
        model.updateCurrentDate(selectedDate)
        refreshDateStats()
        refreshDateSeekButtons()
    }
    
    @Subscribe
    fun onTodayButtonClicked(event: RawDataGeneralStatsView.TodayButtonClickedEvent) {
        model.updateCurrentDate(Date())
        refreshDateStats()
        refreshDateSeekButtons()
    }
    
    @Subscribe
    fun onRangeSelected(event: RawDataGeneralStatsView.RangeSelectedEvent) {
        model.selectedRange = event.position
        model.buildDataSets()
    }
    
    @Subscribe
    fun onDataSetsBuilt(event: RawDataGeneralStatsModel.DataSetsBuiltEvent) {
        view.setChartsData(event.dataSets)
    }

    private fun refreshDateSeekButtons() {
        if (model.maxDateReached) {
            view.disableForwardButtons()
        } else {
            view.enableForwardButtons()
        }
        if (model.minDateReached) {
            view.disableBackButtons()
        } else {
            view.enableBackButtons()
        }
    }

    private fun refreshDateStats() {
        val statsForDate = model.getStatsForDate()
        view.setDate(model.currentDate)
        view.getStatsValues(
            newCases = statsForDate.newCasesAdjusted.formatNumberString(),
            totalCases = statsForDate.totalCases.formatNumberString(),
            ctiCases = statsForDate.totalCti.formatNumberString(),
            activeCases = statsForDate.inCourse.formatNumberString(),
            recoveredCases = statsForDate.newRecovered.formatNumberString(),
            totalRecovered = statsForDate.totalRecovered.formatNumberString(),
            deceases = statsForDate.newDeceases.formatNumberString(),
            totalDeceases = statsForDate.totalDeceases.formatNumberString(),
            newTests = statsForDate.newTests.formatNumberString(),
            positivity = statsForDate.positivity.formatNumberString(),
            harvardIndex = statsForDate.harvardIndex.formatNumberString(),
            indexVariation = statsForDate.indexVariation.formatNumberString()
        )
        view.setChartSelectedItem(model.getXValueForDate())
    }

    override fun onResume() {
        super.onResume()
        refreshDateSeekButtons()
        view.refreshSelectedRawStats(model.selectedStats)
        model.buildDataSets()
        view.withActivity {
            view.setRangesAdapter(ArrayAdapter(this, R.layout.simple_spinner_layout, RangeUtils.dateRanges))
            view.setSelectedRange(model.selectedRange)
        }
        refreshDateStats()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                view.activity?.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("current_date", model.currentDate)
        outState.putBoolean("max_date_reached", model.maxDateReached)
        outState.putBoolean("min_date_reached", model.minDateReached)
        outState.putSerializable("selected_stats", model.selectedStats)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            model.currentDate = it.getSerializable("current_date") as Date
            model.maxDateReached = it.getBoolean("max_date_reached", true)
            model.minDateReached = it.getBoolean("min_date_reached", false)
            val selectedStats = it.getSerializable("selected_stats") as ArrayList<Stat>
            model.updateSelectedStats(selectedStats)
        }
    }
}
