package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.CreditsModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.CreditsPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.CreditsView

class CreditsActivity : BaseActivity<CreditsPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.credits_activity)

        presenter = CreditsPresenter(
            CreditsView(this),
            CreditsModel()
        )
    }
}
