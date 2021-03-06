package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.GeneralStatsModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.GeneralStatsPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.GeneralStatsView

class GeneralStatsActivity : BaseActivity<GeneralStatsPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_without_city_selector)

        presenter = GeneralStatsPresenter(
            GeneralStatsView(this),
            GeneralStatsModel()
        )
    }
}
