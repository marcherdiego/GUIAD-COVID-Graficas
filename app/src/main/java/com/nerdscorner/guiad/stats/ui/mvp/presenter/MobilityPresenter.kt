package com.nerdscorner.guiad.stats.ui.mvp.presenter

import com.nerdscorner.guiad.stats.ui.mvp.model.MobilityModel
import com.nerdscorner.guiad.stats.ui.mvp.view.MobilityView
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class MobilityPresenter(view: MobilityView, model: MobilityModel) : StatsPresenter<MobilityView, MobilityModel>(view, model) {
    
    @Subscribe
    fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        model.buildDataSets()
    }
}
