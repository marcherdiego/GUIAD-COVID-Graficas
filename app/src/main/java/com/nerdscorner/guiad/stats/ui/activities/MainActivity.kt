package com.nerdscorner.guiad.stats.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.MainModel
import com.nerdscorner.guiad.stats.ui.mvp.presenter.MainPresenter
import com.nerdscorner.guiad.stats.ui.mvp.view.MainView

class MainActivity : BaseActivity<MainPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        presenter = MainPresenter(
            MainView(this),
            MainModel()
        )
    }
}
