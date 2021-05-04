package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.extensions.format
import com.nerdscorner.covid.stats.utils.SharedPreferencesUtils

class GeneralStatsData private constructor() : DataObject() {
    fun getDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        return getDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor)
    }

    override fun getStats() = listOf(
        inCourseStat,
        newCasesAdjustedStat,
        newCasesOriginalStat,
        newDeceasesStat,
        totalDeceasesStat,
        totalCtiStat,
        newRecoveredStat,
        totalRecoveredStat,
        newTestsStat,
        totalTestsStat,
        totalCasesStat,
        medicalDischargesStat,
        reportedOutOfDateStat,
        positivityStat
    )

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveGeneralData(data)
    }

    fun getStatsForDate(filterDate: String, filterPreviousDayDate: String): StatsForDate {
        val dataLine = dataLines.firstOrNull { it.split(COMMA)[INDEX_DATE] == filterDate }
        return if (dataLine == null) {
            StatsForDate()
        } else {
            val dateP7Stats = P7Data.getInstance().getStatsForDate(filterDate)
            val previousDayP7Stats = P7Data.getInstance().getStatsForDate(filterPreviousDayDate)
            val dataTokens = dataLine.split(COMMA)
            StatsForDate(
                inCourse = dataTokens[INDEX_IN_COURSE],
                newCasesAdjusted = dataTokens[INDEX_NEW_CASES_ADJUSTED],
                newCasesOriginal = dataTokens[INDEX_NEW_CASES_ORIGINAL],
                totalCases = dataTokens[INDEX_TOTAL_CASES],
                newDeceases = dataTokens[INDEX_NEW_DECEASES],
                totalDeceases = dataTokens[INDEX_TOTAL_DECEASES],
                totalCti = dataTokens[INDEX_TOTAL_CTI],
                newRecovered = dataTokens[INDEX_NEW_RECOVERED],
                totalRecovered = dataTokens[INDEX_TOTAL_RECOVERED],
                newTests = dataTokens[INDEX_NEW_TESTS],
                totalTests = dataTokens[INDEX_TOTAL_TESTS],
                medicalDischarges = dataTokens[INDEX_MEDICAL_DISCHARGES],
                reportedOutOfDate = dataTokens[INDEX_REPORTED_OUT_OF_DATE],
                positivity = dataTokens[INDEX_POSITIVITY],
                harvardIndex = dateP7Stats.p7,
                indexVariation = getStatDelta(dateP7Stats.p7, previousDayP7Stats.p7)
            )
        }

    }

    private fun getStatDelta(stat1: String, stat2: String): String {
        return try {
            val delta = stat1.toFloat() - stat2.toFloat()
            val sign = when {
                delta > 0 -> "+"
                else -> ""
            }
            "$sign${delta.format()}"
        } catch (e: Exception) {
            "N/A"
        }
    }

    data class StatsForDate(
        val inCourse: String = "N/A",
        val newCasesAdjusted: String = "N/A",
        val newCasesOriginal: String = "N/A",
        val totalCases: String = "N/A",
        val newDeceases: String = "N/A",
        val totalDeceases: String = "N/A",
        val totalCti: String = "N/A",
        val newRecovered: String = "N/A",
        val totalRecovered: String = "N/A",
        val newTests: String = "N/A",
        val totalTests: String = "N/A",
        val medicalDischarges: String = "N/A",
        val reportedOutOfDate: String = "N/A",
        var positivity: String = "N/A",
        val harvardIndex: String = "N/A",
        val indexVariation: String = "N/A"
    ) {
        init {
            positivity = try {
                (positivity.toFloat() * positivityStat.factor).format(2)
            } catch (e: Exception) {
                positivity
            }
        }
    }

    companion object {
        private val instance = GeneralStatsData()

        fun getInstance() = instance

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

        val inCourseStat = Stat("Casos en curso", INDEX_IN_COURSE)
        val newCasesAdjustedStat = Stat("Nuevos casos (Ajustado)", INDEX_NEW_CASES_ADJUSTED)
        val newCasesOriginalStat = Stat("Nuevos casos (Original)", INDEX_NEW_CASES_ORIGINAL)
        val newDeceasesStat = Stat("Nuevos fallecidos", INDEX_NEW_DECEASES)
        val totalDeceasesStat = Stat("Total de fallecidos", INDEX_TOTAL_DECEASES)
        val totalCtiStat = Stat("Total en CTI", INDEX_TOTAL_CTI)
        val newRecoveredStat = Stat("Nuevos recuperados", INDEX_NEW_RECOVERED)
        val totalRecoveredStat = Stat("Total de recuperados", INDEX_TOTAL_RECOVERED)
        val newTestsStat = Stat("Tests realizados", INDEX_NEW_TESTS)
        val totalTestsStat = Stat("Total tests realizados", INDEX_TOTAL_TESTS)
        val totalCasesStat = Stat("Total de casos", INDEX_TOTAL_CASES)
        val medicalDischargesStat = Stat("Altas médicas", INDEX_MEDICAL_DISCHARGES)
        val reportedOutOfDateStat = Stat("Reportados fuera de fecha", INDEX_REPORTED_OUT_OF_DATE)
        val positivityStat = Stat("Positividad (%)", INDEX_POSITIVITY, factor = 100f)
    }
}