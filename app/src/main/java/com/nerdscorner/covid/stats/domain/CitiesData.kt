package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class CitiesData(csvLines: List<String>) : DataObject(csvLines) {

    fun getDataSet(dataIndex: Int, selectedCities: List<String>, @ColorInt color: Int, @ColorInt valueTextColor: Int): ILineDataSet {
        val dataLines = csvLines
            .asSequence()
            .drop(1) //Drop header
            .groupBy {
                it.split(COMMA)[INDEX_DATE]
            }
            .map { dateEntries ->
                val date = dateEntries.key
                val valuesSum = dateEntries
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
                return@map "$date,$valuesSum"
            }
        return getDataSet(headers, dataLines, 0, 1, color, valueTextColor)
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

        const val TODOS = "Todo el país"
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
            TODOS,
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

        fun getStats() = listOf(
            Stat("Casos en curso", INDEX_IN_COURSE),
            Stat("Nuevos casos (calculado)", INDEX_NEW_CASES_CALCULATED),
            Stat("Nuevos casos (App)", INDEX_NEW_CASES_APP),
            Stat("Nuevos fallecidos", INDEX_NEW_DECEASES),
            Stat("Total de fallecidos", INDEX_TOTAL_DECEASES),
            Stat("Nuevos recuperados", INDEX_NEW_RECOVERED),
            Stat("Total de recuperados", INDEX_TOTAL_RECOVERED),
            Stat("Total de casos", INDEX_TOTAL_CASES)
        )
    }
}
