package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.covid.stats.ui.mvp.model.DeceasesStatsModel
import com.nerdscorner.covid.stats.ui.mvp.view.DeceasesStatsView
import com.nerdscorner.covid.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class DeceasesStatsPresenter(view: DeceasesStatsView, model: DeceasesStatsModel) :
    StatsPresenter<DeceasesStatsView, DeceasesStatsModel>(view, model) {
    
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
