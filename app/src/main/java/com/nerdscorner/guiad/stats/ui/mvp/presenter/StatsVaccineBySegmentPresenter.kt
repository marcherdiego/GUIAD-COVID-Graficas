package com.nerdscorner.guiad.stats.ui.mvp.presenter

import com.nerdscorner.guiad.stats.ui.mvp.model.StatsVaccinesBySegmentModel
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsVaccinesBySegmentView
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class StatsVaccineBySegmentPresenter(view: StatsVaccinesBySegmentView, model: StatsVaccinesBySegmentModel) :
    StatsPresenter<StatsVaccinesBySegmentView, StatsVaccinesBySegmentModel>(view, model) {

    @Subscribe
    fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        model.buildDataSets()
    }
}
