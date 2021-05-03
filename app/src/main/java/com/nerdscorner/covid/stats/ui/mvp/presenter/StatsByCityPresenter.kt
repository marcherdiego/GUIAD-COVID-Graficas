package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.covid.stats.ui.mvp.model.StatsByCityModel
import com.nerdscorner.covid.stats.ui.mvp.view.StatsByCityView
import com.nerdscorner.covid.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class StatsByCityPresenter(view: StatsByCityView, model: StatsByCityModel) :
    StatsPresenter<StatsByCityView, StatsByCityModel>(view, model) {

    @Subscribe
    fun onCitySelected(event: StatsView.CitySelectedEvent) {
        model.selectedCity = event.position
        view.setChartsData(model.getDataSet())
    }

    @Subscribe
    fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        view.setChartsData(model.getDataSet())
    }
}
