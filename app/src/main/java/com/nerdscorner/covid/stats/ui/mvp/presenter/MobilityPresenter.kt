package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.covid.stats.ui.mvp.model.MobilityModel
import com.nerdscorner.covid.stats.ui.mvp.view.MobilityView
import com.nerdscorner.covid.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class MobilityPresenter(view: MobilityView, model: MobilityModel) : StatsPresenter<MobilityView, MobilityModel>(view, model) {
    
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
