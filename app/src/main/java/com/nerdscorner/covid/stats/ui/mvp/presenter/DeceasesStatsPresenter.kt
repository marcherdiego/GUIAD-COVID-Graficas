package com.nerdscorner.covid.stats.ui.mvp.presenter

import android.widget.ArrayAdapter
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.mvp.model.DeceasesStatsModel
import com.nerdscorner.covid.stats.ui.mvp.view.DeceasesStatsView
import org.greenrobot.eventbus.Subscribe

class DeceasesStatsPresenter(view: DeceasesStatsView, model: DeceasesStatsModel) :
    StatsPresenter<DeceasesStatsView, DeceasesStatsModel>(view, model) {
    
    init {
        view.withActivity {
            view.setStatsAdapter(ArrayAdapter(this, R.layout.simple_spinner_layout, model.availableStats))
        }
    }
    
    @Subscribe
    fun onStatIndexSelectedEvent(event: DeceasesStatsView.StatIndexSelectedEvent) {
        model.setSelectedStatIndex(event.position)
        view.setChartsData(model.getDataSet(graphColors))
    }
}
