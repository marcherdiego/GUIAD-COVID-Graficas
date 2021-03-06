package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.StatsCtiModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.StatsCtiPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsCtiView

class StatsCtiActivity : BaseActivity<StatsCtiPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_without_city_selector)

        presenter = StatsCtiPresenter(
            StatsCtiView(this),
            StatsCtiModel()
        )
    }
}
