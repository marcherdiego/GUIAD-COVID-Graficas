package com.nerdscorner.covid.stats.ui.mvp.view

import com.nerdscorner.covid.stats.R
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.covid.stats.ui.activities.MainActivity

class MainView(activity: MainActivity) : BaseActivityView(activity) {
    init {
        onClick(R.id.cti_button, CtiButtonClickedEvent())
        onClick(R.id.cities_button, CitiesButtonClickedEvent())
    }

    class CtiButtonClickedEvent
    class CitiesButtonClickedEvent
}
