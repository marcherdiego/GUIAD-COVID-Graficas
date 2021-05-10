package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.StatsVaccinesBySegmentModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.StatsVaccineBySegmentPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsVaccinesBySegmentView

class StatsVaccineBySegmentActivity : BaseActivity<StatsVaccineBySegmentPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_without_city_selector)

        presenter = StatsVaccineBySegmentPresenter(
            StatsVaccinesBySegmentView(this),
            StatsVaccinesBySegmentModel()
        )
    }
}
