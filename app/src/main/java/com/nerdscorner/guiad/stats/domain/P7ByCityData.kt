package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils

class P7ByCityData private constructor() : DataObject() {
    private var dataByCity = mapOf<String, Map<String, List<List<String>>>>()

    override fun setData(data: String?) {
        super.setData(data)
        dataByCity = dataLines
            .groupBy { it[INDEX_CITY] }
            .mapValues {
                it
                    .value
                    .groupBy { it[INDEX_DATE] }
            }
    }

    fun getLineDataSet(
        stat: Stat,
        selectedCities: List<String>,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int? = null,
        mode: LineDataSet.Mode? = null
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
                listOf(date, valuesSum.toString())
            }
        return getLineDataSet(dataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor, limit, mode)
    }

    fun getBarDataSet(
        stat: Stat,
        selectedCities: List<String>,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int? = null
    ): IBarDataSet {
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
                listOf(date, valuesSum.toString())
            }
        return getBarDataSet(dataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor, limit)
    }

    override fun getStats() = listOf(p7Stat, p7Per100KStat)

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveP7ByCityData(data)
    }

    override fun getLatestUpdate() = getValueAt(dataLines.size - 1, dateStat) ?: N_A

    companion object {
        private val instance = P7ByCityData()

        fun getInstance() = instance

        private const val INDEX_DATE = 0
        private const val INDEX_CITY = 1
        private const val INDEX_P7 = 5
        private const val INDEX_P7_PER_100K = 6

        private val dateStat = Stat("Date", INDEX_DATE)
        val p7Stat = Stat("Valor p7 (por departamento)", INDEX_P7)
        val p7Per100KStat = Stat("Índice p7/100.000 habitantes (por departamento)", INDEX_P7_PER_100K)
    }
}