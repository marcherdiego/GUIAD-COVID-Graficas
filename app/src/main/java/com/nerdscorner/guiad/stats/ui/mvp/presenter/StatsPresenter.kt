package com.nerdscorner.guiad.stats.ui.mvp.presenter

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.domain.ChartType
import com.nerdscorner.guiad.stats.domain.Stat
import com.nerdscorner.guiad.stats.ui.adapter.StatsAdapter
import com.nerdscorner.guiad.stats.ui.mvp.model.StatsModel
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsView
import com.nerdscorner.guiad.stats.utils.RangeUtils
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import org.greenrobot.eventbus.Subscribe

abstract class StatsPresenter<V : StatsView, M : StatsModel>(view: V, model: M) : BaseActivityPresenter<V, M>(view, model) {

    @Subscribe
    fun onRangeSelected(event: StatsView.RangeSelectedEvent) {
        model.selectedRange = event.position
        model.buildDataSets()
    }

    @Subscribe
    fun onLineDataSetsBuilt(event: StatsModel.LineDataSetsBuiltEvent) {
        view.setLineChartData(event.dataSets)
    }

    @Subscribe
    fun onBarDataSetsBuilt(event: StatsModel.BarDataSetsBuiltEvent) {
        view.setBarChartData(event.dataSets)
    }

    @Subscribe
    fun onChartTypeSelected(event: StatsView.ChartTypeSelectedEvent) {
        model.chartType = event.chartType
        when (model.chartType) {
            ChartType.LINE -> {
                view.showLineChart()
                view.hideBarChart()
            }
            ChartType.BAR -> {
                view.showBarChart()
                view.hideLineChart()
            }
        }
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
        outState.putInt("selected_city", model.selectedCity)
        outState.putSerializable("selected_stats", model.selectedStats)
        outState.putSerializable("selected_chart_type", model.chartType)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            model.selectedCity = it.getInt("selected_city")
            val selectedStats = it.getSerializable("selected_stats") as ArrayList<Stat>
            model.selectedStats.addAll(
                model.availableStats.filter { availableStat ->
                    availableStat.name in selectedStats.map { selectedStat ->
                        selectedStat.name
                    }
                }
            )
            model.chartType = (it.getSerializable("selected_chart_type") as? ChartType) ?: ChartType.LINE
        }
    }

    override fun onResume() {
        super.onResume()
        model.availableStats.forEach { stat ->
            stat.selected = stat in model.selectedStats
        }
        view.withActivity {
            view.setCitiesAdapter(ArrayAdapter(this, R.layout.simple_spinner_layout, model.allCities))
            view.setStatsAdapter(StatsAdapter(this, model.getStatsStateList()))
            view.setRangesAdapter(ArrayAdapter(this, R.layout.simple_spinner_layout, RangeUtils.dateRanges))

            view.setSelectedCity(model.selectedCity)
            view.setSelectedRange(model.selectedRange)
            view.setSelectedChartType(model.chartType)
        }
        model.buildDataSets()
    }
}
