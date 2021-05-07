package com.nerdscorner.guiad.stats.ui.mvp.presenter

import com.nerdscorner.guiad.stats.ui.mvp.model.P7StatsModel
import com.nerdscorner.guiad.stats.ui.mvp.view.P7StatsView
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class P7StatsPresenter(view: P7StatsView, model: P7StatsModel) : StatsPresenter<P7StatsView, P7StatsModel>(view, model) {

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
