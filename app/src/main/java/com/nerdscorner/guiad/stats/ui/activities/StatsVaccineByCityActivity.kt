package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.StatsVaccinesByCityModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.StatsVaccineByCityPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsVaccinesByCityView

class StatsVaccineByCityActivity : BaseActivity<StatsVaccineByCityPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_with_city_selector)

        presenter = StatsVaccineByCityPresenter(
            StatsVaccinesByCityView(this),
            StatsVaccinesByCityModel()
        )
    }
}
