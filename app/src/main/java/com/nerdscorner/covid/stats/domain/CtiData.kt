package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class CtiData(csvLines: List<String>) : DataObject(csvLines) {

    fun getDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        return getDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor)
    }

    override fun getStats() = listOf(
        Stat("Pacientes internados", INDEX_PATIENTS_QUANTITY),
        Stat("Ocupación total (%)", INDEX_TOTAL_OCCUPATION),
        Stat("Ocupación por Covid (%)", INDEX_COVID_OCCUPATION),
        Stat("Camas operativas", INDEX_OPERATIVE_BEDS),
        Stat("Camas ocupadas", INDEX_OCCUPIED_BEDS),
        Stat("Nuevos pacientes", INDEX_NEW_PATIENTS),
        Stat("Defunciones", INDEX_DECEASES),
        Stat("Altas médicas", INDEX_MEDICAL_DISCHARGES)
    )

    companion object {
        const val DATA_FILE_NAME = "data/estadisticasUY_cti.csv"

        private const val INDEX_DATE = 1
        private const val INDEX_PATIENTS_QUANTITY = 2
        private const val INDEX_TOTAL_OCCUPATION = 3
        private const val INDEX_COVID_OCCUPATION = 4
        private const val INDEX_OPERATIVE_BEDS = 5
        private const val INDEX_OCCUPIED_BEDS = 6
        private const val INDEX_NEW_PATIENTS = 7
        private const val INDEX_DECEASES = 8
        private const val INDEX_MEDICAL_DISCHARGES = 9
    }
}