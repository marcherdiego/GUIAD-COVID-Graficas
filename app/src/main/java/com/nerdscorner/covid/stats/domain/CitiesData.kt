package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class CitiesData(csvLines: List<String>) : DataObject(csvLines) {

    fun getDataSet(dataIndex: Int, selectedCities: List<String>, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        val dataLines = csvLines
            .asSequence()
            .drop(1)
            .groupBy {
                it.split(COMMA)[INDEX_DATE]
            }
            .map { dateEntries ->
                dateEntries
                    .value
                    .map {
                        val dataTokens = it.split(COMMA)
                        val city = dataTokens[INDEX_CITY]
                        if (city in selectedCities) {
                            dataTokens[dataIndex].toInt()
                        } else {
                            0
                        }
                    }
                    .reduce { acc, s -> acc + s }
                    .toString()
            }
        return getDataSet(headers, dataLines, 0, 0, color, valueTextColor)
    }

    companion object {
        private const val INDEX_DATE = 0
        const val INDEX_CITY = 1
        const val INDEX_IN_COURSE = 2
        const val INDEX_NEW_CASES_CALCULATED = 3
        const val INDEX_NEW_CASES_APP = 4
        const val INDEX_NEW_DECEASES = 5
        const val INDEX_TOTAL_DECEASES = 6
        const val INDEX_NEW_RECOVERED = 7
        const val INDEX_TOTAL_RECOVERED = 8
        const val INDEX_TOTAL_CASES = 9

        const val ARTIGAS = "Artigas(UY-AR)"
        const val CANELONES = "Canelones(UY-CA)"
        const val CERRO_LARGO = "Cerro Largo(UY-CL)"
        const val COLONIA = "Colonia(UY-CO)"
        const val DURAZNO = "Durazno(UY-DU)"
        const val FLORES = "Flores(UY-FS)"
        const val FLORIDA = "Florida(UY-FD)"
        const val LAVALLEJA = "Lavalleja(UY-LA)"
        const val MALDONADO = "Maldonado(UY-MA)"
        const val MONTEVIDEO = "Montevideo(UY-MO)"
        const val PAYSANDU = "Paysandú(UY-PA)"
        const val RIO_NEGRO = "Río Negro(UY-RN)"
        const val RIVERA = "Rivera(UY-RV)"
        const val ROCHA = "Rocha(UY-RO)"
        const val SALTO = "Salto(UY-SA)"
        const val SAN_JOSE = "San José(UY-SJ)"
        const val SORIANO = "Soriano(UY-SO)"
        const val TACUAREMBO = "Tacuarembó(UY-TA)"
        const val TREINTA_Y_TRES = "Treinta y Tres(UY-TT)"

        fun getAllCities() = listOf(
            ARTIGAS,
            CANELONES,
            CERRO_LARGO,
            COLONIA,
            DURAZNO,
            FLORES,
            FLORIDA,
            LAVALLEJA,
            MALDONADO,
            MONTEVIDEO,
            PAYSANDU,
            RIO_NEGRO,
            RIVERA,
            ROCHA,
            SALTO,
            SAN_JOSE,
            SORIANO,
            TACUAREMBO,
            TREINTA_Y_TRES
        )

        fun getDataIndexes() = listOf(
            INDEX_IN_COURSE,
            INDEX_NEW_CASES_CALCULATED,
            INDEX_NEW_CASES_APP,
            INDEX_NEW_DECEASES,
            INDEX_TOTAL_DECEASES,
            INDEX_NEW_RECOVERED,
            INDEX_TOTAL_RECOVERED,
            INDEX_TOTAL_CASES
        )
    }
}
