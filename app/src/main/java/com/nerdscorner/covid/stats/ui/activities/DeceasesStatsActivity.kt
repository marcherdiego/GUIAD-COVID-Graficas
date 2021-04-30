package com.nerdscorner.covid.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.mvp.model.DeceasesStatsModel
import com.nerdscorner.covid.stats.ui.mvp.presenter.DeceasesStatsPresenter
import com.nerdscorner.covid.stats.ui.mvp.view.DeceasesStatsView

class DeceasesStatsActivity : BaseActivity<DeceasesStatsPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deceases_stats_activity)

        presenter = DeceasesStatsPresenter(
            DeceasesStatsView(this),
            DeceasesStatsModel()
        )
    }
}
