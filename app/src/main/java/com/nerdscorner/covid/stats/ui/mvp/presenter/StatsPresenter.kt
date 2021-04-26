package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter

import com.nerdscorner.covid.stats.ui.mvp.model.StatsModel
import com.nerdscorner.covid.stats.ui.mvp.view.StatsView

class StatsPresenter(view: StatsView, model: StatsModel) :
    BaseActivityPresenter<StatsView, StatsModel>(view, model) {

}
