package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils

class P7ByCityData private constructor() : DataObject() {
    private lateinit var dataByCity: Map<String, Map<String, List<List<String>>>>

    override fun setData(data: String?) {
        super.setData(data)
        dataByCity = dataLines
            .groupBy { it.split(COMMA)[INDEX_CITY] }
            .mapValues {
                it
                    .value
                    .map { it.split(COMMA) }
                    .groupBy { it[INDEX_DATE] }
            }
    }

    fun getDataSet(
        stat: Stat,
        selectedCities: List<String>,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int? = null
    ): ILineDataSet {
        val dataLines = dataByCity
            .filter { it.key in selectedCities }
            .flatMap { dataByCity ->
                dataByCity.value.map { dataByCityAndDate ->
                    val date = dataByCityAndDate.key
                    val valuesSum = dataByCityAndDate
                        .value
                        .map { it[stat.index].toFloatOrNull() ?: 0f }
                        .reduce { acc, v -> acc + v }
                    Pair(date, valuesSum)
                }
            }
            .groupBy { it.first }
            .map {
                val date = it.key
                var valuesSum = 0f
                it.value.forEach {
                    valuesSum += it.second
                }
                listOf(date, valuesSum).joinToString()
            }
        return getDataSet(dataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor, limit)
    }

    override fun getStats() = listOf(p7Stat, p7Per100KStat)

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveP7ByCityData(data)
    }

    companion object {
        private val instance = P7ByCityData()

        fun getInstance() = instance

        private const val INDEX_DATE = 0
        private const val INDEX_CITY = 1
        private const val INDEX_P7 = 5
        private const val INDEX_P7_PER_100K = 6

        val p7Stat = Stat("Valor p7 (por departamento)", INDEX_P7)
        val p7Per100KStat = Stat("Índice p7/100.000 habitantes (por departamento)", INDEX_P7_PER_100K)
    }
}