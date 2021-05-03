package com.nerdscorner.covid.stats.ui.mvp.presenter

import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.domain.Stat
import com.nerdscorner.covid.stats.ui.adapter.StatsAdapter
import com.nerdscorner.covid.stats.ui.mvp.model.StatsModel
import com.nerdscorner.covid.stats.ui.mvp.view.StatsView
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter

abstract class StatsPresenter<V : StatsView, M : StatsModel>(view: V, model: M) : BaseActivityPresenter<V, M>(view, model) {
    init {
        view.withActivity {
            view.setCitiesAdapter(ArrayAdapter(this, R.layout.simple_spinner_layout, model.allCities))
            view.setStatsAdapter(StatsAdapter(this, model.getStatsStateList()))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                view.activity?.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
