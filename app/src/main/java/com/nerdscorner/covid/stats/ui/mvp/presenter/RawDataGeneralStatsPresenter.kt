package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter

import com.nerdscorner.covid.stats.ui.mvp.model.RawDataGeneralStatsModel
import com.nerdscorner.covid.stats.ui.mvp.view.RawDataGeneralStatsView
import org.greenrobot.eventbus.Subscribe
import java.util.*

class RawDataGeneralStatsPresenter(view: RawDataGeneralStatsView, model: RawDataGeneralStatsModel) :
    BaseActivityPresenter<RawDataGeneralStatsView, RawDataGeneralStatsModel>(view, model) {
    init {
        view.setMinDate(RawDataGeneralStatsModel.MIN_DATE)
        view.setDate(Date())
        view.setNewCases(model.getNewCases(Date()))
    }

    @Subscribe
    fun onDatePicked(event: RawDataGeneralStatsView.DatePickedEvent) {
        view.setNewCases(model.getNewCases(event.date))
    }
}
