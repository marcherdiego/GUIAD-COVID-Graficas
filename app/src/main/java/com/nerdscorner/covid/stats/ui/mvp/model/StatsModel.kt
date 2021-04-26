package com.nerdscorner.covid.stats.ui.mvp.model

import android.app.Activity
import com.nerdscorner.mvplib.events.model.BaseEventsModel

abstract class StatsModel : BaseEventsModel() {
    abstract val dataFileName: String

    protected lateinit var csvLines: List<String>

    open fun loadChartsData(activity: Activity) {
        csvLines = getAssetsFileLines(activity, dataFileName)
    }
    
    private fun getAssetsFileLines(activity: Activity, fileName: String): List<String> {
        return activity
            .assets
            .open(fileName)
            .bufferedReader()
            .use {
                it.readText()
            }
            .split(NEW_LINE)
    }

    companion object {
        private const val NEW_LINE = "\n"
    }
}
