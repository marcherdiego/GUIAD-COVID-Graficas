package com.nerdscorner.guiad.stats.ui.mvp.presenter

import com.nerdscorner.guiad.stats.ui.mvp.model.GeneralStatsModel
import com.nerdscorner.guiad.stats.ui.mvp.view.GeneralStatsView
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class GeneralStatsPresenter(view: GeneralStatsView, model: GeneralStatsModel) :
    StatsPresenter<GeneralStatsView, GeneralStatsModel>(view, model) {

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
