package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class CtiData private constructor() : DataObject() {

    fun getDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        return getDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor)
    }

    override fun getStats() = listOf(
        patientsQuantityStat,
        totalOccupationStat,
        covidOccupationStat,
        operativeBedsStat,
        occupiedBedsStat,
        newPatientsStat,
        deceasesStat,
        medicalDischargesStat
    )

    companion object {
        private val instance = CtiData()

        fun getInstance() = instance

        private const val INDEX_DATE = 1
        private const val INDEX_PATIENTS_QUANTITY = 2
        private const val INDEX_TOTAL_OCCUPATION = 3
        private const val INDEX_COVID_OCCUPATION = 4
        private const val INDEX_OPERATIVE_BEDS = 5
        private const val INDEX_OCCUPIED_BEDS = 6
        private const val INDEX_NEW_PATIENTS = 7
        private const val INDEX_DECEASES = 8
        private const val INDEX_MEDICAL_DISCHARGES = 9

        val patientsQuantityStat = Stat("Pacientes internados", INDEX_PATIENTS_QUANTITY)
        val totalOccupationStat = Stat("Ocupación total (%)", INDEX_TOTAL_OCCUPATION)
        val covidOccupationStat = Stat("Ocupación por Covid (%)", INDEX_COVID_OCCUPATION)
        val operativeBedsStat = Stat("Camas operativas", INDEX_OPERATIVE_BEDS)
        val occupiedBedsStat = Stat("Camas ocupadas", INDEX_OCCUPIED_BEDS)
        val newPatientsStat = Stat("Nuevos pacientes", INDEX_NEW_PATIENTS)
        val deceasesStat = Stat("Defunciones", INDEX_DECEASES)
        val medicalDischargesStat = Stat("Altas médicas", INDEX_MEDICAL_DISCHARGES)
    }
}