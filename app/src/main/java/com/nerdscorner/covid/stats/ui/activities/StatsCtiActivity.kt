package com.nerdscorner.covid.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.mvp.model.StatsCtiModel
import com.nerdscorner.covid.stats.ui.mvp.presenter.StatsCtiPresenter
import com.nerdscorner.covid.stats.ui.mvp.view.StatsCtiView

class StatsCtiActivity : BaseActivity<StatsCtiPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stats_cti_activity)

        presenter = StatsCtiPresenter(
            StatsCtiView(this),
            StatsCtiModel()
        )
    }
}
