package com.nerdscorner.covid.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.mvp.model.GeneralStatsModel
import com.nerdscorner.covid.stats.ui.mvp.presenter.GeneralStatsPresenter
import com.nerdscorner.covid.stats.ui.mvp.view.GeneralStatsView

class GeneralStatsActivity : BaseActivity<GeneralStatsPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.general_stats_activity)

        presenter = GeneralStatsPresenter(
            GeneralStatsView(this),
            GeneralStatsModel()
        )
    }
}
