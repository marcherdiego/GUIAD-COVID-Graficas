package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.DateUtils
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils

class VaccinesByCityData private constructor() : DataObject() {

    override fun setData(data: String?) {
        super.setData(data?.replace(csvStringLiteralsRegex, EMPTY_STRING))
        dataLines.forEach { dataTokens ->
            dataTokens[INDEX_VACCINE] = EMPTY_STRING
            dataTokens[INDEX_DATE] = DateUtils.convertUsDateToUyDate(dataTokens[INDEX_DATE])
        }
    }

    fun getLineDataSet(
        stat: Stat,
        selectedCity: String,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int? = null
    ): ILineDataSet {
        val statOffset = getStatOffset(selectedCity)
        return getLineDataSet(dataLines, INDEX_DATE, stat.index + statOffset, stat.factor, stat.name, color, valueTextColor, limit)
    }

    fun getBarDataSet(
        stat: Stat,
        selectedCity: String,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int? = null
    ): IBarDataSet {
        val statOffset = getStatOffset(selectedCity)
        return getBarDataSet(dataLines, INDEX_DATE, stat.index + statOffset, stat.factor, stat.name, color, valueTextColor, limit)
    }

    fun getLineDataSetAbsoluteIndex(
        stat: Stat,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int? = null
    ): ILineDataSet {
        return getLineDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor, limit)
    }

    fun getBarDataSetAbsoluteIndex(
        stat: Stat,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int? = null
    ): IBarDataSet {
        return getBarDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor, limit)
    }

    private fun getStatOffset(selectedCity: String): Int {
        return when (selectedCity) {
            ARTIGAS -> DATA_OFFSET_ARTIGAS
            CANELONES -> DATA_OFFSET_CANELONES
            CERRO_LARGO -> DATA_OFFSET_CERRO_LARGO
            COLONIA -> DATA_OFFSET_COLONIA
            DURAZNO -> DATA_OFFSET_DURAZNO
            FLORIDA -> DATA_OFFSET_FLORIDA
            FLORES -> DATA_OFFSET_FLORES
            LAVALLEJA -> DATA_OFFSET_LAVALLEJA
            MALDONADO -> DATA_OFFSET_MALDONADO
            MONTEVIDEO -> DATA_OFFSET_MONTEVIDEO
            PAYSANDU -> DATA_OFFSET_PAYSANDU
            RIO_NEGRO -> DATA_OFFSET_RIO_NEGRO
            ROCHA -> DATA_OFFSET_ROCHA
            RIVERA -> DATA_OFFSET_RIVERA
            SALTO -> DATA_OFFSET_SALTO
            SAN_JOSE -> DATA_OFFSET_SAN_JOSE
            SORIANO -> DATA_OFFSET_SORIANO
            TACUAREMBO -> DATA_OFFSET_TACUAREMBO
            TREINTA_Y_TRES -> DATA_OFFSET_TREINTA_Y_TRES
            else -> DATA_OFFSET_MONTEVIDEO
        }
    }

    override fun getStats() = listOf(
        dailyVaccinationsStat,
        totalPeopleVaccinatedStat,
        dailyPeopleVaccinatedStat,
        totalPeopleFullyVaccinatedStat
    )

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveVaccinesGlobalData(data)
    }

    companion object {
        private val csvStringLiteralsRegex = "\".*\"".toRegex()
        private val instance = VaccinesByCityData()

        fun getInstance() = instance

        private const val INDEX_DATE = 1
        private const val INDEX_VACCINE = 2

        // Offsets
        private const val DATA_OFFSET_ARTIGAS = 27
        private const val DATA_OFFSET_CANELONES = 31
        private const val DATA_OFFSET_CERRO_LARGO = 35
        private const val DATA_OFFSET_COLONIA = 39
        private const val DATA_OFFSET_DURAZNO = 43
        private const val DATA_OFFSET_FLORIDA = 47
        private const val DATA_OFFSET_FLORES = 51
        private const val DATA_OFFSET_LAVALLEJA = 55
        private const val DATA_OFFSET_MALDONADO = 59
        private const val DATA_OFFSET_MONTEVIDEO = 63
        private const val DATA_OFFSET_PAYSANDU = 67
        private const val DATA_OFFSET_RIO_NEGRO = 71
        private const val DATA_OFFSET_ROCHA = 75
        private const val DATA_OFFSET_RIVERA = 79
        private const val DATA_OFFSET_SALTO = 83
        private const val DATA_OFFSET_SAN_JOSE = 87
        private const val DATA_OFFSET_SORIANO = 91
        private const val DATA_OFFSET_TACUAREMBO = 95
        private const val DATA_OFFSET_TREINTA_Y_TRES = 99

        private const val ARTIGAS = "Artigas"
        private const val CANELONES = "Canelones"
        private const val CERRO_LARGO = "Cerro Largo"
        private const val COLONIA = "Colonia"
        private const val DURAZNO = "Durazno"
        private const val FLORES = "Flores"
        private const val FLORIDA = "Florida"
        private const val LAVALLEJA = "Lavalleja"
        private const val MALDONADO = "Maldonado"
        const val MONTEVIDEO = "Montevideo"
        private const val PAYSANDU = "Paysandú"
        private const val RIO_NEGRO = "Río Negro"
        private const val RIVERA = "Rivera"
        private const val ROCHA = "Rocha"
        private const val SALTO = "Salto"
        private const val SAN_JOSE = "San José"
        private const val SORIANO = "Soriano"
        private const val TACUAREMBO = "Tacuarembó"
        private const val TREINTA_Y_TRES = "Treinta y Tres"

        val dailyVaccinationsStat = Stat("Vacunaciones (por día)", 0)
        val totalPeopleVaccinatedStat = Stat("Total vacunados (acumulado)", 1)
        val dailyPeopleVaccinatedStat = Stat("Vacunados (por día)", 2)
        val totalPeopleFullyVaccinatedStat = Stat("Completamente vacunados (acumulado)", 3)

        val dailyVaccinationsMontevideoStat = Stat("Actos vacunales", DATA_OFFSET_MONTEVIDEO)
    }
}
