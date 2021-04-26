package com.nerdscorner.covid.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.mvp.model.StatsByCityModel
import com.nerdscorner.covid.stats.ui.mvp.presenter.StatsByCityPresenter
import com.nerdscorner.covid.stats.ui.mvp.view.StatsByCityView

class StatsByCityActivity : BaseActivity<StatsByCityPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stats_by_city_activity)

        presenter = StatsByCityPresenter(
            StatsByCityView(this),
            StatsByCityModel()
        )
    }
}
