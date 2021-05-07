package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.MobilityModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.MobilityPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.MobilityView

class MobilityActivity : BaseActivity<MobilityPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_without_city_selector)

        presenter = MobilityPresenter(
            MobilityView(this),
            MobilityModel()
        )
    }
}
