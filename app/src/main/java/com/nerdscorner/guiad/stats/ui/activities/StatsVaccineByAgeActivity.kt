package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.StatsVaccinesByAgeModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.StatsVaccineByAgePresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsVaccinesByAgeView

class StatsVaccineByAgeActivity : BaseActivity<StatsVaccineByAgePresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_without_city_selector)

        presenter = StatsVaccineByAgePresenter(
            StatsVaccinesByAgeView(this),
            StatsVaccinesByAgeModel()
        )
    }
}
