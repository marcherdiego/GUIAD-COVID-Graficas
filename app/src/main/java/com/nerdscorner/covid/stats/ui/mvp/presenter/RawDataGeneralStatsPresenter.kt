package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.covid.stats.domain.GeneralStatsData
import com.nerdscorner.covid.stats.domain.P7Data
import com.nerdscorner.covid.stats.extensions.formatNumberString
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter

import com.nerdscorner.covid.stats.ui.mvp.model.RawDataGeneralStatsModel
import com.nerdscorner.covid.stats.ui.mvp.view.RawDataGeneralStatsView
import org.greenrobot.eventbus.Subscribe

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
        refreshDateStats()
    }

    @Subscribe
    fun onDatePicked(event: RawDataGeneralStatsView.DatePickedEvent) {
        model.currentDate = event.date
        refreshDateStats()
    }

    @Subscribe
    fun onBackDayButtonClicked(event: RawDataGeneralStatsView.BackDayButtonClickedEvent) {
        model.setDayOffset(-1)
        refreshDateStats()
    }

    @Subscribe
    fun onBackMonthButtonClicked(event: RawDataGeneralStatsView.BackMonthButtonClickedEvent) {
        model.setMonthOffset(-1)
        refreshDateStats()
    }

    @Subscribe
    fun onForwardDayButtonClicked(event: RawDataGeneralStatsView.ForwardDayButtonClickedEvent) {
        model.setDayOffset(1)
        refreshDateStats()
    }

    @Subscribe
    fun onForwardMonthButtonClicked(event: RawDataGeneralStatsView.ForwardMonthButtonClickedEvent) {
        model.setMonthOffset(1)
        refreshDateStats()
    }

    @Subscribe
    fun onStatClicked(event: RawDataGeneralStatsView.StatClickedEvent) {
        model.updateSelectedStats(event.rawStat)
        view.setChartsData(model.getDataSet())
    }

    private fun refreshDateStats() {
        val statsForDate = model.getStatsForDate()
        view.setDate(model.currentDate)
        view.getStatsValues(
            newCases = statsForDate.newRecovered.formatNumberString(),
            totalCases = statsForDate.totalCases.formatNumberString(),
            ctiCases = statsForDate.totalCti.formatNumberString(),
            activeCases = statsForDate.totalCases.formatNumberString(),
            recoveredCases = statsForDate.newRecovered.formatNumberString(),
            totalRecovered = statsForDate.totalRecovered.formatNumberString(),
            deceases = statsForDate.newDeceases.formatNumberString(),
            totalDeceases = statsForDate.totalDeceases.formatNumberString(),
            newTests = statsForDate.newTests.formatNumberString(),
            positivity = statsForDate.positivity.formatNumberString(),
            harvardIndex = statsForDate.harvardIndex.formatNumberString(),
            indexVariation = statsForDate.indexVariation.formatNumberString()
        )
    }
}
