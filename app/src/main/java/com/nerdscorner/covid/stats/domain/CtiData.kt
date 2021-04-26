package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class CtiData(csvLines: List<String>) : DataObject(csvLines) {

    fun getDataSet(dataIndex: Int, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        return getDataSet(headers, dataLines, INDEX_DATE, dataIndex, color, valueTextColor)
    }

    companion object {
        private const val INDEX_DATE = 1
        const val INDEX_PATIENTS_QUANTITY = 2
        const val INDEX_TOTAL_OCCUPATION = 3
        const val INDEX_COVID_OCCUPATION = 4
        const val INDEX_OPERATIVE_BEDS = 5
        const val INDEX_OCCUPIED_BEDS = 6
        const val INDEX_NEW_PATIENTS = 7
        const val INDEX_DECEASES = 8
        const val INDEX_MEDICAL_DISCHARGES = 9

        fun getDataIndexes() = listOf(
            INDEX_PATIENTS_QUANTITY,
            INDEX_TOTAL_OCCUPATION,
            INDEX_COVID_OCCUPATION,
            INDEX_OPERATIVE_BEDS,
            INDEX_OCCUPIED_BEDS,
            INDEX_NEW_PATIENTS,
            INDEX_DECEASES,
            INDEX_MEDICAL_DISCHARGES
        )
    }
}