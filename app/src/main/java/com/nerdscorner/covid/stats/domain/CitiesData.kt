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
        return getDataSet(dataLines, 0, 1, color, valueTextColor)
    }

    override val dataOffset = 1

    override fun getStats() = listOf(
        Stat("Casos en curso", INDEX_IN_COURSE),
        Stat("Nuevos casos (calculado)", INDEX_NEW_CASES_CALCULATED),
        Stat("Nuevos casos (App)", INDEX_NEW_CASES_APP),
        Stat("Nuevos fallecidos", INDEX_NEW_DECEASES),
        Stat("Total de fallecidos", INDEX_TOTAL_DECEASES),
        Stat("Nuevos recuperados", INDEX_NEW_RECOVERED),
        Stat("Total de recuperados", INDEX_TOTAL_RECOVERED),
        Stat("Total de casos", INDEX_TOTAL_CASES)
    )

    companion object {
        const val DATA_FILE_NAME = "data/estadisticasUY_porDepto_detalle.csv"
        
        private const val INDEX_DATE = 0
        private const val INDEX_CITY = 1
        private const val INDEX_IN_COURSE = 2
        private const val INDEX_NEW_CASES_CALCULATED = 3
        private const val INDEX_NEW_CASES_APP = 4
        private const val INDEX_NEW_DECEASES = 5
        private const val INDEX_TOTAL_DECEASES = 6
        private const val INDEX_NEW_RECOVERED = 7
        private const val INDEX_TOTAL_RECOVERED = 8
        private const val INDEX_TOTAL_CASES = 9

        private const val TODOS = "Todo el país"
        private const val ARTIGAS = "Artigas(UY-AR)"
        private const val CANELONES = "Canelones(UY-CA)"
        private const val CERRO_LARGO = "Cerro Largo(UY-CL)"
        private const val COLONIA = "Colonia(UY-CO)"
        private const val DURAZNO = "Durazno(UY-DU)"
        private const val FLORES = "Flores(UY-FS)"
        private const val FLORIDA = "Florida(UY-FD)"
        private const val LAVALLEJA = "Lavalleja(UY-LA)"
        private const val MALDONADO = "Maldonado(UY-MA)"
        private const val MONTEVIDEO = "Montevideo(UY-MO)"
        private const val PAYSANDU = "Paysandú(UY-PA)"
        private const val RIO_NEGRO = "Río Negro(UY-RN)"
        private const val RIVERA = "Rivera(UY-RV)"
        private const val ROCHA = "Rocha(UY-RO)"
        private const val SALTO = "Salto(UY-SA)"
        private const val SAN_JOSE = "San José(UY-SJ)"
        private const val SORIANO = "Soriano(UY-SO)"
        private const val TACUAREMBO = "Tacuarembó(UY-TA)"
        private const val TREINTA_Y_TRES = "Treinta y Tres(UY-TT)"

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
    }
}
