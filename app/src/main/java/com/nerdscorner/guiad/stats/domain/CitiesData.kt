package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.extensions.formatNumberString
import com.nerdscorner.guiad.stats.extensions.roundToString
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils

class CitiesData private constructor() : DataObject() {
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

    fun getDataSet(
        stat: Stat,
        selectedCities: List<String>,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int? = null
    ): ILineDataSet {
        val dataLines = getDataLinesForCities(stat, selectedCities)
        return getDataSet(dataLines, 0, 1, Stat.DEFAULT_FACTOR, stat.name, color, valueTextColor, limit)
    }

    private fun getDataLinesForCities(stat: Stat, selectedCities: List<String>): List<List<String>> {
        return dataByCity
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
    }

    override fun getLatestValue(stat: Stat): String {
        val dataLines = getDataLinesForCities(stat, getAllCities().drop(1))
        val latestValue = getValueAt(dataLines, dataLines.size - 1)?.times(stat.factor) ?: return N_A
        return latestValue.roundToString().formatNumberString()
    }

    override fun isTrendingUp(stat: Stat): Boolean {
        val dataLines = getDataLinesForCities(stat, getAllCities().drop(1))
        if (dataLines.size < 2) {
            return false
        }
        val latestValue = getValueAt(dataLines, dataLines.size - 1)?.times(stat.factor) ?: 0f
        val preLatestValue = getValueAt(dataLines, dataLines.size - 2)?.times(stat.factor) ?: 0f
        return (latestValue - preLatestValue) > 0
    }

    private fun getValueAt(dataLines: List<List<String>>, row: Int) = dataLines.getOrNull(row)?.get(1)?.toFloatOrNull()

    override fun getStats() = listOf(
        inCourseStat,
        newCasesCalculatedStat,
        newCasesAppStat,
        newDeceasesStat,
        totalDeceasesStat,
        newRecoveredStat,
        totalRecoveredStat,
        totalCasesStat
    )

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveCitiesData(data)
    }

    companion object {
        private val instance = CitiesData()

        fun getInstance() = instance

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

        val inCourseStat = Stat("Casos en curso", INDEX_IN_COURSE)
        val newCasesCalculatedStat = Stat("Nuevos casos (calculado)", INDEX_NEW_CASES_CALCULATED)
        val newCasesAppStat = Stat("Nuevos casos (App)", INDEX_NEW_CASES_APP)
        val newDeceasesStat = Stat("Nuevos fallecidos", INDEX_NEW_DECEASES)
        val totalDeceasesStat = Stat("Total de fallecidos", INDEX_TOTAL_DECEASES)
        val newRecoveredStat = Stat("Nuevos recuperados", INDEX_NEW_RECOVERED)
        val totalRecoveredStat = Stat("Total de recuperados", INDEX_TOTAL_RECOVERED)
        val totalCasesStat = Stat("Total de casos", INDEX_TOTAL_CASES)

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

        fun getAllCitiesNames() = listOf(
            "Artigas",
            "Canelones",
            "Cerro Largo",
            "Colonia",
            "Durazno",
            "Flores",
            "Florida",
            "Lavalleja",
            "Maldonado",
            "Montevideo",
            "Paysandú",
            "Río Negro",
            "Rivera",
            "Rocha",
            "Salto",
            "San José",
            "Soriano",
            "Tacuarembó",
            "Treinta y Tres"
        )

        fun getAllCitiesNamesIncludingAll() = getAllCitiesNames().toMutableList().apply { add(0, TODOS) }
    }
}
