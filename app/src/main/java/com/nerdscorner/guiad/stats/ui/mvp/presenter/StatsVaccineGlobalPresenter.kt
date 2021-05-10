package com.nerdscorner.guiad.stats.ui.mvp.presenter

import com.nerdscorner.guiad.stats.ui.mvp.model.StatsVaccinesGlobalModel
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsVaccinesGlobalView
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class StatsVaccineGlobalPresenter(view: StatsVaccinesGlobalView, model: StatsVaccinesGlobalModel) :
    StatsPresenter<StatsVaccinesGlobalView, StatsVaccinesGlobalModel>(view, model) {

    @Subscribe
    fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        model.buildDataSets()
    }
}
