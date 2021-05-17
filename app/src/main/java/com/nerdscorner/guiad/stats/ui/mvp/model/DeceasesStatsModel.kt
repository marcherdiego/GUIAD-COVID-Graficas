package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.domain.CitiesData
import com.nerdscorner.guiad.stats.domain.DeceasesData
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.guiad.stats.extensions.runAsync
import com.nerdscorner.guiad.stats.extensions.withResult
import org.greenrobot.eventbus.ThreadMode

class DeceasesStatsModel : StatsModel() {
    private var deceasesData = DeceasesData.getInstance()

    init {
        allCities = CitiesData.getAllCitiesNamesIncludingAll()
    }

    override val availableStats by lazy { deceasesData.getStats() }

    override suspend fun createLineDataSets(): List<ILineDataSet> {
        return runAsync {
            val selectedCities = if (selectedCity == 0) {
                allCities
            } else {
                listOf(allCities[selectedCity])
            }
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                deceasesData.getLineDataSet(stat, selectedCities, chartColor, chartColor)
            }
        }.await()
    }

    override suspend fun createBarDataSets(): List<IBarDataSet> {
        return runAsync {
            val selectedCities = if (selectedCity == 0) {
                allCities
            } else {
                listOf(allCities[selectedCity])
            }
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                deceasesData.getBarDataSet(stat, selectedCities, chartColor, chartColor)
            }
        }.await()
    }
}
