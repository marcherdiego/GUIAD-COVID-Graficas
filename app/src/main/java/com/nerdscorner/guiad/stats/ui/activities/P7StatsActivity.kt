package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.P7StatsModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.P7StatsPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.P7StatsView

class P7StatsActivity : BaseActivity<P7StatsPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_with_city_selector)

        presenter = P7StatsPresenter(
            P7StatsView(this),
            P7StatsModel()
        )
    }
}
