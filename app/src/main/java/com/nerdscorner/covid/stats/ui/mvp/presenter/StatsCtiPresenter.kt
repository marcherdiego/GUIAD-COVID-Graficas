package com.nerdscorner.covid.stats.ui.mvp.presenter

import android.widget.ArrayAdapter
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.mvp.model.StatsCtiModel
import com.nerdscorner.covid.stats.ui.mvp.view.StatsCtiView
import com.nerdscorner.covid.stats.ui.mvp.view.StatsView
import org.greenrobot.eventbus.Subscribe

class StatsCtiPresenter(view: StatsCtiView, model: StatsCtiModel) : StatsPresenter<StatsCtiView, StatsCtiModel>(view, model) {
    init {
        view.withActivity {
            view.setCitiesAdapter(ArrayAdapter(this, R.layout.simple_spinner_layout, listOf(model.allCities.first())))
        }
    }

    @Subscribe
    fun onStatSelected(event: StatsView.StatsSelectedEvent) {
        model.selectedStats = event.selectedStats
        view.setChartsData(model.getDataSet())
    }

    override fun onRangeSelected(event: StatsView.RangeSelectedEvent) {
        super.onRangeSelected(event)
        view.setChartsData(model.getDataSet())
    }

    override fun onResume() {
        super.onResume()
        view.setChartsData(model.getDataSet())
    }
}
