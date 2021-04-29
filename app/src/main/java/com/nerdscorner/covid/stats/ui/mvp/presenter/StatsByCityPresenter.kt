package com.nerdscorner.covid.stats.ui.mvp.presenter

import android.widget.ArrayAdapter
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.mvp.model.StatsByCityModel
import com.nerdscorner.covid.stats.ui.mvp.view.StatsByCityView
import org.greenrobot.eventbus.Subscribe

class StatsByCityPresenter(view: StatsByCityView, model: StatsByCityModel) :
    StatsPresenter<StatsByCityView, StatsByCityModel>(view, model) {
    init {
        view.withActivity {
            view.setChartsData(model.getCityDataSets(graphColors))
            view.setCitiesAdapter(ArrayAdapter(this, R.layout.spinner_item, model.allCities))
            view.setStatsAdapter(ArrayAdapter(this, R.layout.spinner_item, model.availableStats))
        }
    }

    @Subscribe
    fun onCitySelected(event: StatsByCityView.CitySelectedEvent) {
        model.selectedCity = event.position
        view.setChartsData(model.getCityDataSets(graphColors))
    }

    @Subscribe
    fun onStatSelected(event: StatsByCityView.StatSelectedEvent) {
        model.selectedStat = event.position
        view.setChartsData(model.getCityDataSets(graphColors))
    }
}
