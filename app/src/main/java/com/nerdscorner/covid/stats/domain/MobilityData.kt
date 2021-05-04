package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.utils.SharedPreferencesUtils

class MobilityData private constructor() : DataObject() {

    fun getDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        return if (stat == mobilityIndexStat) {
            val dataLines = dataLines.map { line ->
                val dataTokens = line.split(COMMA)
                val date = dataTokens[INDEX_DATE]
                val transit = dataTokens[INDEX_TRANSIT_STATIONS].toFloatOrNull() ?: 0f
                val retailAndRecreation = dataTokens[INDEX_RETAIL_AND_RECREATION].toFloatOrNull() ?: 0f
                val residential = dataTokens[INDEX_RESIDENTIAL].toFloatOrNull() ?: 0f
                val mobilityIndex = 0.5f * (transit + retailAndRecreation - residential)
                "$date,$mobilityIndex"
            }
            getDataSet(dataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor)
        } else {
            getDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor)
        }
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
        SharedPreferencesUtils.saveCtiData(data)
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