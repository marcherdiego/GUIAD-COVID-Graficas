package com.nerdscorner.guiad.stats.ui.mvp.presenter

import com.nerdscorner.guiad.stats.ui.mvp.model.StatsVaccinesByAgeModel
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsVaccinesByAgeView
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class StatsVaccineByAgePresenter(view: StatsVaccinesByAgeView, model: StatsVaccinesByAgeModel) :
    StatsPresenter<StatsVaccinesByAgeView, StatsVaccinesByAgeModel>(view, model) {

    override fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        model.buildDataSets()
    }
}
