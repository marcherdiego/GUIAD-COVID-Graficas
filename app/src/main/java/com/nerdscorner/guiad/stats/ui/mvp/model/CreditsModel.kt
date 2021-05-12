package com.nerdscorner.guiad.stats.ui.mvp.model

import com.nerdscorner.mvplib.events.model.BaseEventsModel

class CreditsModel : BaseEventsModel() {
    fun getGuiadUrl() = "https://guiad-covid.github.io/"
    
    fun getDavidGiordanoUrl() = "https://vacuna.uy/"
}
