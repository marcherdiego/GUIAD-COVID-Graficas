package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter

import com.nerdscorner.covid.stats.ui.mvp.model.CreditsModel
import com.nerdscorner.covid.stats.ui.mvp.view.CreditsView

class CreditsPresenter(view: CreditsView, model: CreditsModel) :
    BaseActivityPresenter<CreditsView, CreditsModel>(view, model) {
    init {
        view.loadUrl("https://guiad-covid.github.io/integrantes/")
    }
}