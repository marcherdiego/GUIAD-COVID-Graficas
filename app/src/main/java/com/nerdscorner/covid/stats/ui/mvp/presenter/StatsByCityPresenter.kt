package com.nerdscorner.covid.stats.ui.mvp.presenter

import android.widget.ArrayAdapter
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.domain.CitiesData
import com.nerdscorner.covid.stats.ui.mvp.model.StatsByCityModel
import com.nerdscorner.covid.stats.ui.mvp.view.StatsByCityView
import org.greenrobot.eventbus.Subscribe

class StatsByCityPresenter(view: StatsByCityView, model: StatsByCityModel) :
    StatsPresenter<StatsByCityView, StatsByCityModel>(view, model) {
    init {
        view.withActivity {
            view.setChartsData(model.getCitiesDataSets(CitiesData.getAllCities(), graphColors))
            view.setCitiesAdapter(ArrayAdapter(this, R.layout.spinner_item, model.getCities()))
        }
    }
    
    @Subscribe
    fun onCitySelected(event: StatsByCityView.CitySelectedEvent) {
        val city = model.getCities()[event.position]
        val cityDataSet = model.getCitiesDataSets(listOf(city), graphColors)
        view.setChartsData(cityDataSet)
    }
}
