package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.covid.stats.ui.activities.StatsByCityActivity
import com.nerdscorner.covid.stats.ui.activities.StatsCtiActivity
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter

import com.nerdscorner.covid.stats.ui.mvp.model.MainModel
import com.nerdscorner.covid.stats.ui.mvp.view.MainView
import org.greenrobot.eventbus.Subscribe

class MainPresenter(view: MainView, model: MainModel) : BaseActivityPresenter<MainView, MainModel>(view, model) {
    @Subscribe
    fun onCtiButtonClicked(event: MainView.CtiButtonClickedEvent) {
        startActivity(StatsCtiActivity::class.java)
    }

    @Subscribe
    fun onCitiesButtonClicked(event: MainView.CitiesButtonClickedEvent) {
        startActivity(StatsByCityActivity::class.java)
    }
}
