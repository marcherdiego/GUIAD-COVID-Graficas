package com.nerdscorner.covid.stats

import android.app.Application
import com.nerdscorner.covid.stats.domain.*
import com.nerdscorner.covid.stats.utils.SharedPreferencesUtils

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtils.init(this)
        
        loadCachedData()
    }

    private fun loadCachedData() {
        CitiesData.getInstance().setData(SharedPreferencesUtils.getCitiesData())
        CtiData.getInstance().setData(SharedPreferencesUtils.getCtiData())
        DeceasesData.getInstance().setData(SharedPreferencesUtils.getDeceasesData())
        GeneralStatsData.getInstance().setData(SharedPreferencesUtils.getGeneralData())
        P7ByCityData.getInstance().setData(SharedPreferencesUtils.getP7ByCityData())
        P7Data.getInstance().setData(SharedPreferencesUtils.getP7Data())
    }
}
