package com.nerdscorner.covid.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.mvp.model.RawDataGeneralStatsModel
import com.nerdscorner.covid.stats.ui.mvp.presenter.RawDataGeneralStatsPresenter
import com.nerdscorner.covid.stats.ui.mvp.view.RawDataGeneralStatsView

class RawDataGeneralStatsActivity : BaseActivity<RawDataGeneralStatsPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.raw_data_general_stats_activity)

        presenter = RawDataGeneralStatsPresenter(
                RawDataGeneralStatsView(this),
                RawDataGeneralStatsModel()
        )
    }
}
