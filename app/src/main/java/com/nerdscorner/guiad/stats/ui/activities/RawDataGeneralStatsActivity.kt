package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.RawDataGeneralStatsModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.RawDataGeneralStatsPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.RawDataGeneralStatsView

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
