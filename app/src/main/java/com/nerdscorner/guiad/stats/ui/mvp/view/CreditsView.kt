package com.nerdscorner.guiad.stats.ui.mvp.view

import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.guiad.stats.ui.activities.CreditsActivity

import com.nerdscorner.guiad.stats.R

class CreditsView(activity: CreditsActivity) : BaseActivityView(activity) {
    init {
        activity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        onClick(R.id.guiad_view_more_button, GuiadViewMoreButtonClickedEvent())
        onClick(R.id.david_giordano_view_more_button, DavidGiordanoViewMoreButtonClickedEvent())
    }

    class GuiadViewMoreButtonClickedEvent
    class DavidGiordanoViewMoreButtonClickedEvent
}
