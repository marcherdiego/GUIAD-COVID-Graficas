package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.guiad.stats.extensions.runAsync
import com.nerdscorner.guiad.stats.extensions.withResult
import com.nerdscorner.guiad.stats.domain.CitiesData
import com.nerdscorner.guiad.stats.domain.VaccinesByCityData
import org.greenrobot.eventbus.ThreadMode

class StatsVaccinesByCityModel : StatsModel() {
    private var vaccinesByCityData = VaccinesByCityData.getInstance()

    override val availableStats by lazy { vaccinesByCityData.getStats() }

    init {
        allCities = CitiesData.getAllCitiesNames()
    }

    override suspend fun createLineDataSets(): List<ILineDataSet> {
        return runAsync {
            val selectedCity = allCities[selectedCity]
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                vaccinesByCityData.getLineDataSet(stat, selectedCity, chartColor, chartColor)
            }
        }.await()
    }

    override suspend fun createBarDataSets(): List<IBarDataSet> {
        return runAsync {
            val selectedCity = allCities[selectedCity]
            selectedStats.map { stat ->
                val chartColor = ColorUtils.getColor(stat.index)
                vaccinesByCityData.getBarDataSet(stat, selectedCity, chartColor, chartColor)
            }
        }.await()
    }
}
