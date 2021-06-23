package com.nerdscorner.guiad.stats.ui.mvp.presenter

import com.nerdscorner.guiad.stats.ui.mvp.model.GeneralStatsModel
import com.nerdscorner.guiad.stats.ui.mvp.view.GeneralStatsView
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class GeneralStatsPresenter(view: GeneralStatsView, model: GeneralStatsModel) :
    StatsPresenter<GeneralStatsView, GeneralStatsModel>(view, model) {

    override fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        model.buildDataSets()
    }
}
