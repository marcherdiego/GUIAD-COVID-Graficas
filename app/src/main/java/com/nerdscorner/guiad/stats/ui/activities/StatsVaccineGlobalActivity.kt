package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.StatsVaccinesGlobalModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.StatsVaccineGlobalPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsVaccinesGlobalView

class StatsVaccineGlobalActivity : BaseActivity<StatsVaccineGlobalPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_without_city_selector)

        presenter = StatsVaccineGlobalPresenter(
            StatsVaccinesGlobalView(this),
            StatsVaccinesGlobalModel()
        )
    }
}
