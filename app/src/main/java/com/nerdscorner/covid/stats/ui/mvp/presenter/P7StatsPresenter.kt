package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.covid.stats.ui.mvp.model.P7StatsModel
import com.nerdscorner.covid.stats.ui.mvp.view.P7StatsView
import com.nerdscorner.covid.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class P7StatsPresenter(view: P7StatsView, model: P7StatsModel) :
    StatsPresenter<P7StatsView, P7StatsModel>(view, model) {

    @Subscribe
    fun onCitySelected(event: StatsView.CitySelectedEvent) {
        model.selectedCity = event.position
        view.setChartsData(model.getDataSet(graphColors))
    }

    @Subscribe
    fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        view.setChartsData(model.getDataSet(graphColors))
    }
}
