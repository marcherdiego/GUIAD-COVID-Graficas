package com.nerdscorner.covid.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.mvp.model.StatsModel
import com.nerdscorner.covid.stats.ui.mvp.presenter.StatsPresenter
import com.nerdscorner.covid.stats.ui.mvp.view.StatsView

class StatsActivity : BaseActivity<StatsPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stats_activity)

        presenter = StatsPresenter(
            StatsView(this),
            StatsModel()
        )
    }
}
