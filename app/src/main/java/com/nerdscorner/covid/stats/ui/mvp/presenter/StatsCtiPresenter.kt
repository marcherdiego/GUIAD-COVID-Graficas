package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.covid.stats.ui.mvp.model.StatsCtiModel
import com.nerdscorner.covid.stats.ui.mvp.view.StatsCtiView

class StatsCtiPresenter(view: StatsCtiView, model: StatsCtiModel) : StatsPresenter<StatsCtiView, StatsCtiModel>(view, model) {
    init {
        view.withActivity {
            view.setChartsData(model.getAllDataSets(graphColors))
        }
    }
}
