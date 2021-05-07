package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.StatsByCityModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.StatsByCityPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsByCityView

class StatsByCityActivity : BaseActivity<StatsByCityPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_with_city_selector)

        presenter = StatsByCityPresenter(
            StatsByCityView(this),
            StatsByCityModel()
        )
    }
}
