package com.nerdscorner.covid.stats.ui.mvp.presenter

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
    protected val graphColors = mutableListOf<@ColorInt Int>()

    init {
        view.withActivity {
            model.loadChartsData(this)
            graphColors.add(ContextCompat.getColor(this, R.color.graph1))
            graphColors.add(ContextCompat.getColor(this, R.color.graph2))
            graphColors.add(ContextCompat.getColor(this, R.color.graph3))
            graphColors.add(ContextCompat.getColor(this, R.color.graph4))
            graphColors.add(ContextCompat.getColor(this, R.color.graph5))
            graphColors.add(ContextCompat.getColor(this, R.color.graph6))
            graphColors.add(ContextCompat.getColor(this, R.color.graph7))
            graphColors.add(ContextCompat.getColor(this, R.color.graph8))
            graphColors.add(ContextCompat.getColor(this, R.color.graph9))

            view.setCitiesAdapter(ArrayAdapter(this, R.layout.simple_spinner_layout, model.allCities))
            view.setStatsAdapter(StatsAdapter(this, model.getStatsStateList()))
        }
    }
}
