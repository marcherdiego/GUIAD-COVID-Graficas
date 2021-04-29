package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class GeneralStatsData(csvLines: List<String>) : DataObject(csvLines) {
    fun getDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        return getDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor)
    }

    override fun getStats() = listOf(
        Stat("Casos en curso", INDEX_IN_COURSE),
        Stat("Nuevos casos (Ajustado)", INDEX_NEW_CASES_ADJUSTED),
        Stat("Nuevos casos (Original)", INDEX_NEW_CASES_ORIGINAL),
        Stat("Nuevos fallecidos", INDEX_NEW_DECEASES),
        Stat("Total de fallecidos", INDEX_TOTAL_DECEASES),
        Stat("Total en CTI", INDEX_TOTAL_CTI),
        Stat("Nuevos recuperados", INDEX_NEW_RECOVERED),
        Stat("Total de recuperados", INDEX_TOTAL_RECOVERED),
        Stat("Tests realizados", INDEX_NEW_TESTS),
        Stat("Total tests realizados", INDEX_TOTAL_TESTS),
        Stat("Total de casos", INDEX_TOTAL_CASES),
        Stat("Altas médicas", INDEX_MEDICAL_DISCHARGES),
        Stat("Reportados fuera de fecha", INDEX_REPORTED_OUT_OF_DATE),
        Stat("Positividad (%)", INDEX_POSITIVITY, factor = 100f)
    )

    companion object {
        const val DATA_FILE_NAME = "data/estadisticasUY.csv"

        private const val INDEX_DATE = 0
        private const val INDEX_DAY = 1
        private const val INDEX_IN_COURSE = 2
        private const val INDEX_NEW_CASES_ADJUSTED = 3
        private const val INDEX_NEW_CASES_ORIGINAL = 4
        private const val INDEX_TOTAL_CASES = 5
        private const val INDEX_NEW_DECEASES = 6
        private const val INDEX_TOTAL_DECEASES = 7
        private const val INDEX_TOTAL_CTI = 8
        private const val INDEX_TOTAL_CI = 9
        private const val INDEX_NEW_RECOVERED = 10
        private const val INDEX_TOTAL_RECOVERED = 11
        private const val INDEX_NEW_TESTS = 12
        private const val INDEX_TOTAL_TESTS = 13
        private const val INDEX_DAY_WEEK = 14
        private const val INDEX_MEDICAL_DISCHARGES = 15
        private const val INDEX_REPORTED_OUT_OF_DATE = 16
        private const val INDEX_POSITIVITY = 17
    }
}