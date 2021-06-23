package com.nerdscorner.guiad.stats.ui.mvp.presenter

import com.nerdscorner.guiad.stats.ui.mvp.model.StatsVaccinesByCityModel
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsVaccinesByCityView
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class StatsVaccineByCityPresenter(view: StatsVaccinesByCityView, model: StatsVaccinesByCityModel) :
    StatsPresenter<StatsVaccinesByCityView, StatsVaccinesByCityModel>(view, model) {

    @Subscribe
    fun onCitySelected(event: StatsView.CitySelectedEvent) {
        model.selectedCity = event.position
        model.buildDataSets()
    }

    override fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        model.buildDataSets()
    }
}
