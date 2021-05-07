package com.nerdscorner.guiad.stats.ui.mvp.presenter

import android.widget.ArrayAdapter
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.mvp.model.StatsCtiModel
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsCtiView
import com.nerdscorner.guiad.stats.ui.mvp.view.StatsView
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
        model.buildDataSets()
    }

    override fun onRangeSelected(event: StatsView.RangeSelectedEvent) {
        super.onRangeSelected(event)
        model.buildDataSets()
    }

    override fun onResume() {
        super.onResume()
        model.buildDataSets()
    }
}
