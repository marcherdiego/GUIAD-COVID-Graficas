package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.DeceasesStatsModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.DeceasesStatsPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.DeceasesStatsView

class DeceasesStatsActivity : BaseActivity<DeceasesStatsPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_with_city_selector)

        presenter = DeceasesStatsPresenter(
            DeceasesStatsView(this),
            DeceasesStatsModel()
        )
    }
}
