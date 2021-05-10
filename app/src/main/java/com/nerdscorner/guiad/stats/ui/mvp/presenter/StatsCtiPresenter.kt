package com.nerdscorner.guiad.stats.ui.mvp.presenter

import com.nerdscorner.guiad.stats.ui.mvp.model.StatsCtiModel
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsCtiView
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class StatsCtiPresenter(view: StatsCtiView, model: StatsCtiModel) : StatsPresenter<StatsCtiView, StatsCtiModel>(view, model) {

    @Subscribe
    fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        model.buildDataSets()
    }
}
