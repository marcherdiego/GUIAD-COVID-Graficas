package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.extensions.formatNumberString
import com.nerdscorner.guiad.stats.extensions.roundToString
import com.nerdscorner.guiad.stats.utils.DateUtils
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils

class MobilityData private constructor() : DataObject() {
    private var mobilityIndexDataLines = listOf<List<String>>()

    fun getLineDataSet(
        stat: Stat,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int? = null,
        mode: LineDataSet.Mode? = null
    ): ILineDataSet {
        return if (stat == mobilityIndexStat) {
            getLineDataSet(mobilityIndexDataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor, limit, mode)
        } else {
            getLineDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor, limit, mode)
        }
    }

    fun getBarDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int, limit: Int? = null): IBarDataSet {
        return if (stat == mobilityIndexStat) {
            getBarDataSet(mobilityIndexDataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor, limit)
        } else {
            getBarDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor, limit)
        }
    }

    override fun setData(data: String?) {
        super.setData(data)
        dataLines.forEach { dataTokens ->
            dataTokens[INDEX_DATE] = DateUtils.convertUsDateToUyDate(dataTokens[INDEX_DATE])
        }

        mobilityIndexDataLines = dataLines.map { dataTokens ->
            val date = dataTokens[INDEX_DATE]
            val transit = dataTokens[INDEX_TRANSIT_STATIONS].toFloatOrNull() ?: 0f
            val retailAndRecreation = dataTokens[INDEX_RETAIL_AND_RECREATION].toFloatOrNull() ?: 0f
            val residential = dataTokens[INDEX_RESIDENTIAL].toFloatOrNull() ?: 0f
            val mobilityIndex = 0.5f * (transit + retailAndRecreation - residential)
            listOf(date, mobilityIndex.toString())
        }
    }

    override fun getLatestValue(stat: Stat): String {
        return if (stat.index == INDEX_MOBILITY) {
            getMobilityIndexAt(dataLines.size - 1).formatNumberString()
        } else {
            super.getLatestValue(stat)
        }
    }

    override fun isTrendingUp(stat: Stat): Boolean {
        val dataSize = dataLines.size
        if (dataSize < 2) {
            return false
        }
        return if (stat.index == INDEX_MOBILITY) {
            val latestValue = getMobilityIndexAt(dataLines.size - 1).toFloatOrNull() ?: 0f
            val preLatestValue = getMobilityIndexAt(dataLines.size - 2).toFloatOrNull() ?: 0f
            return (latestValue - preLatestValue) > 0
        } else {
            super.isTrendingUp(stat)
        }
    }

    private fun getMobilityIndexAt(index: Int): String {
        val dataTokens = getLineTokensAt(index) ?: return N_A
        val transit = dataTokens[INDEX_TRANSIT_STATIONS].toFloatOrNull() ?: 0f
        val retailAndRecreation = dataTokens[INDEX_RETAIL_AND_RECREATION].toFloatOrNull() ?: 0f
        val residential = dataTokens[INDEX_RESIDENTIAL].toFloatOrNull() ?: 0f
        val mobilityIndex = 0.5f * (transit + retailAndRecreation - residential)
        return mobilityIndex.roundToString()
    }

    override fun getStats() = listOf(
        mobilityIndexStat,
        retailAndRecreationStat,
        groceryAndPharmacyStat,
        parksStat,
        transitStationsStat,
        workplacesStat,
        residentialStat
    )

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveMobilityData(data)
    }

    companion object {
        private val instance = MobilityData()

        fun getInstance() = instance

        // This index is calculated as 0.5 * (INDEX_TRANSIT_STATIONS + INDEX_RETAIL_AND_RECREATION - INDEX_RESIDENTIAL)
        // See: https://guiad-covid.github.io/estadisticasuy.html#Movilidad-en-Uruguay
        private const val INDEX_MOBILITY = 15

        private const val INDEX_DATE = 8
        private const val INDEX_RETAIL_AND_RECREATION = 9
        private const val INDEX_GROCERY_AND_PHARMACY = 10
        private const val INDEX_PARKS = 11
        private const val INDEX_TRANSIT_STATIONS = 12
        private const val INDEX_WORKPLACES = 13
        private const val INDEX_RESIDENTIAL = 14

        val mobilityIndexStat = Stat("Índice de movilidad", INDEX_MOBILITY)
        val retailAndRecreationStat = Stat("Tiendas y espacio de ocio", INDEX_RETAIL_AND_RECREATION)
        val groceryAndPharmacyStat = Stat("Almacenes y farmacias", INDEX_GROCERY_AND_PHARMACY)
        val parksStat = Stat("Parques", INDEX_PARKS)
        val transitStationsStat = Stat("Tránsito", INDEX_TRANSIT_STATIONS)
        val workplacesStat = Stat("Lugares de trabajo", INDEX_WORKPLACES)
        val residentialStat = Stat("Zonas residenciales", INDEX_RESIDENTIAL)
    }
}