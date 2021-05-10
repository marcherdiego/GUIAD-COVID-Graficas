package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils

class VaccinesBySegmentData private constructor() : DataObject() {

    fun getDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int, limit: Int? = null): ILineDataSet {
        return getDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor, limit)
    }

    override fun getStats() = listOf(
        dailyTeachersStat,
        totalTeachersStat,
        dailyElepemStat,
        totalElepemStat,
        dailyChronicStat,
        totalChronicStat,
        dailyUndefinedStat,
        totalUndefinedStat,
        dailyDialysisStat,
        totalDialysisStat,
        dailyHealthStat,
        totalHealthStat,
        dailyDeprivedLibertyStat,
        totalDeprivedLibertyStat,
        dailyEssentialStat,
        totalEssentialStat,
        dailyNoRiskStat,
        totalNoRiskStat,
        dailyPregnantStat,
        totalPregnantStat
    )

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveVaccinesBySegmentData(data)
    }

    companion object {
        private val instance = VaccinesBySegmentData()

        fun getInstance() = instance

        private const val INDEX_DATE = 0
        private const val INDEX_DAILY_TEACHERS = 1
        private const val INDEX_TOTAL_TEACHERS = 2
        private const val INDEX_DAILY_ELEPEM = 3
        private const val INDEX_TOTAL_ELEPEM = 4
        private const val INDEX_DAILY_CHRONIC = 5
        private const val INDEX_TOTAL_CHRONIC = 6
        private const val INDEX_DAILY_UNDEFINED = 7
        private const val INDEX_TOTAL_UNDEFINED = 8
        private const val INDEX_DAILY_DIALYSIS = 9
        private const val INDEX_TOTAL_DIALYSIS = 10
        private const val INDEX_DAILY_HEALTH = 11
        private const val INDEX_TOTAL_HEALTH = 12
        private const val INDEX_DAILY_DEPRIVED_LIBERTY = 13
        private const val INDEX_TOTAL_DEPRIVED_LIBERTY = 14
        private const val INDEX_DAILY_ESSENTIAL = 15
        private const val INDEX_TOTAL_ESSENTIAL = 16
        private const val INDEX_DAILY_NO_RISK = 17
        private const val INDEX_TOTAL_NO_RISK = 18
        private const val INDEX_DAILY_PREGNANT = 19
        private const val INDEX_TOTAL_PREGNANT = 20

        val dailyTeachersStat = Stat("Docentes (por día)", INDEX_DAILY_TEACHERS)
        val totalTeachersStat = Stat("Docentes (acumulado)", INDEX_TOTAL_TEACHERS)
        val dailyElepemStat = Stat("ELEPEM (por día)", INDEX_DAILY_ELEPEM)
        val totalElepemStat = Stat("ELEPEM (acumulado)", INDEX_TOTAL_ELEPEM)
        val dailyChronicStat = Stat("Enfermedad crónica (por día)", INDEX_DAILY_CHRONIC)
        val totalChronicStat = Stat("Enfermedad crónica (acumulado)", INDEX_TOTAL_CHRONIC)
        val dailyUndefinedStat = Stat("Indefinidos (por día)", INDEX_DAILY_UNDEFINED)
        val totalUndefinedStat = Stat("Indefinidos (acumulado)", INDEX_TOTAL_UNDEFINED)
        val dailyDialysisStat = Stat("Pacientes en diálisis (por día)", INDEX_DAILY_DIALYSIS)
        val totalDialysisStat = Stat("Pacientes en diálisis (acumulado)", INDEX_TOTAL_DIALYSIS)
        val dailyHealthStat = Stat("Personal de salud (por día)", INDEX_DAILY_HEALTH)
        val totalHealthStat = Stat("Personal de salud (acumulado)", INDEX_TOTAL_HEALTH)
        val dailyDeprivedLibertyStat = Stat("Privados de libertad (por día)", INDEX_DAILY_DEPRIVED_LIBERTY)
        val totalDeprivedLibertyStat = Stat("Privados de libertad (acumulado)", INDEX_TOTAL_DEPRIVED_LIBERTY)
        val dailyEssentialStat = Stat("Servicios esenciales (por día)", INDEX_DAILY_ESSENTIAL)
        val totalEssentialStat = Stat("Servicios esenciales (acumulado)", INDEX_TOTAL_ESSENTIAL)
        val dailyNoRiskStat = Stat("Fuera de riesgo (por día)", INDEX_DAILY_NO_RISK)
        val totalNoRiskStat = Stat("Fuera de riesgo (acumulado)", INDEX_TOTAL_NO_RISK)
        val dailyPregnantStat = Stat("Embarazadas (por día)", INDEX_DAILY_PREGNANT)
        val totalPregnantStat = Stat("Embarazadas (acumulado)", INDEX_TOTAL_PREGNANT)
    }
}
