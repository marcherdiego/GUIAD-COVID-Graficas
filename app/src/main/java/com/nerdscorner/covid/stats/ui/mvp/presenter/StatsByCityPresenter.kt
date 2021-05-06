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
        model.buildDataSets()
    }

    @Subscribe
    fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        model.buildDataSets()
    }

    override fun onRangeSelected(event: StatsView.RangeSelectedEvent) {
        super.onRangeSelected(event)
        model.buildDataSets()
    }

    override fun onResume() {
        super.onResume()
        model.buildDataSets()
    }
}
