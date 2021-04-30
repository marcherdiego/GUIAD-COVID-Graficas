package com.nerdscorner.covid.stats

import android.app.Application
import com.nerdscorner.covid.stats.utils.SharedPreferencesUtils

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtils.init(this)
    }
}
