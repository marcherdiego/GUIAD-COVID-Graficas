package com.nerdscorner.covid.stats.ui.mvp.presenter

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.domain.Stat
import com.nerdscorner.covid.stats.ui.adapter.StatsAdapter
import com.nerdscorner.covid.stats.ui.mvp.model.StatsModel
import com.nerdscorner.covid.stats.ui.mvp.view.StatsView
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter

abstract class StatsPresenter<V : StatsView, M : StatsModel>(view: V, model: M) : BaseActivityPresenter<V, M>(view, model) {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                view.activity?.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("selected_city", model.selectedCity)
        outState.putSerializable("selected_stats", model.selectedStats)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            model.selectedCity = it.getInt("selected_city")
            val selectedStats = it.getSerializable("selected_stats") as ArrayList<Stat>
            model.selectedStats.addAll(
                model.availableStats.filter { availableStat ->
                    availableStat.name in selectedStats.map { selectedStat ->
                        selectedStat.name
                    }
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        model.availableStats.forEach { stat ->
            stat.selected = stat in model.selectedStats
        }
        view.withActivity {
            view.setCitiesAdapter(ArrayAdapter(this, R.layout.simple_spinner_layout, model.allCities))
            view.setStatsAdapter(StatsAdapter(this, model.getStatsStateList()))
        }
    }
}
