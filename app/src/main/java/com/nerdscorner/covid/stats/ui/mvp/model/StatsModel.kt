package com.nerdscorner.covid.stats.ui.mvp.model

import android.app.Activity
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.domain.CitiesData
import com.nerdscorner.covid.stats.domain.Stat
import com.nerdscorner.mvplib.events.model.BaseEventsModel

abstract class StatsModel : BaseEventsModel() {
    abstract val dataFileName: String

    val allCities = CitiesData.getAllCities()
    abstract val availableStats: List<Stat>

    protected lateinit var csvLines: List<String>

    // State vars
    var selectedCity = 0
    var selectedStats = listOf<Stat>()

    open fun loadChartsData(activity: Activity) {
        csvLines = getAssetsFileLines(activity, dataFileName)
    }

    abstract fun getDataSet(colorsList: List<Int>): List<ILineDataSet>

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

    fun getStatsStateList(): List<Stat> {
        return availableStats
            .toMutableList()
            .apply {
                add(0, Stat("Seleccionar"))
            }
    }

    companion object {
        private const val NEW_LINE = "\n"
    }
}
