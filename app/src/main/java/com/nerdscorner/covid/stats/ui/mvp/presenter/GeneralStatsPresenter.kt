package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.covid.stats.ui.mvp.model.GeneralStatsModel
import com.nerdscorner.covid.stats.ui.mvp.view.GeneralStatsView
import com.nerdscorner.covid.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class GeneralStatsPresenter(view: GeneralStatsView, model: GeneralStatsModel) :
    StatsPresenter<GeneralStatsView, GeneralStatsModel>(view, model) {

    @Subscribe
    fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        view.setChartsData(model.getDataSet())
    }

    override fun onResume() {
        super.onResume()
        view.setChartsData(model.getDataSet())
    }
}
