package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.DateUtils
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils

class VaccinesByAgeData private constructor() : DataObject() {

    override fun setData(data: String?) {
        super.setData(data)
        dataLines.forEach { dataTokens ->
            dataTokens[INDEX_DATE] = DateUtils.convertUsDateToUyDate(dataTokens[INDEX_DATE])
        }
    }

    fun getLineDataSet(
        stat: Stat,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int? = null,
        mode: LineDataSet.Mode? = null
    ): ILineDataSet {
        return getLineDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor, limit, mode)
    }

    fun getBarDataSet(stat: Stat, @ColorInt color: Int, @ColorInt valueTextColor: Int, limit: Int? = null): IBarDataSet {
        return getBarDataSet(dataLines, INDEX_DATE, stat.index, stat.factor, stat.name, color, valueTextColor, limit)
    }

    override fun getStats() = listOf(
        daily18_24Stat,
        total18_24Stat,
        daily25_34Stat,
        total25_34Stat,
        daily35_44Stat,
        total35_44Stat,
        daily45_54Stat,
        total45_54Stat,
        daily55_64Stat,
        total55_64Stat,
        daily65_74Stat,
        total65_74Stat,
        daily75_115Stat,
        total75_115Stat,
        dailyUndefinedStat,
        totalUndefinedStat,
        daily18_49Stat,
        total18_49Stat,
        daily50_70Stat,
        total50_70Stat,
        daily71_79Stat,
        total71_79Stat,
        daily80_115Stat,
        total80_115Stat
    )

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveVaccinesByAgeData(data)
    }

    companion object {
        private val instance = VaccinesByAgeData()

        fun getInstance() = instance

        private const val INDEX_DATE = 0
        private const val INDEX_DAILY_18_24 = 1
        private const val INDEX_TOTAL_18_24 = 2
        private const val INDEX_DAILY_25_34 = 3
        private const val INDEX_TOTAL_25_34 = 4
        private const val INDEX_DAILY_35_44 = 5
        private const val INDEX_TOTAL_35_44 = 6
        private const val INDEX_DAILY_45_54 = 7
        private const val INDEX_TOTAL_45_54 = 8
        private const val INDEX_DAILY_55_64 = 9
        private const val INDEX_TOTAL_55_64 = 10
        private const val INDEX_DAILY_65_74 = 11
        private const val INDEX_TOTAL_65_74 = 12
        private const val INDEX_DAILY_75_115 = 13
        private const val INDEX_TOTAL_75_115 = 14
        private const val INDEX_DAILY_UNDEFINED = 15
        private const val INDEX_TOTAL_UNDEFINED = 16
        private const val INDEX_DAILY_18_49 = 17
        private const val INDEX_TOTAL_18_49 = 18
        private const val INDEX_DAILY_50_70 = 19
        private const val INDEX_TOTAL_50_70 = 20
        private const val INDEX_DAILY_71_79 = 21
        private const val INDEX_TOTAL_71_79 = 22
        private const val INDEX_DAILY_80_115 = 23
        private const val INDEX_TOTAL_80_115 = 24

        val daily18_24Stat = Stat("18 a 24 años (por día)", INDEX_DAILY_18_24)
        val total18_24Stat = Stat("18 a 24 años (acumulado)", INDEX_TOTAL_18_24)
        val daily25_34Stat = Stat("25 a 34 años (por día)", INDEX_DAILY_25_34)
        val total25_34Stat = Stat("25 a 34 años (acumulado)", INDEX_TOTAL_25_34)
        val daily35_44Stat = Stat("35 a 44 años (por día)", INDEX_DAILY_35_44)
        val total35_44Stat = Stat("35 a 44 años (acumulado)", INDEX_TOTAL_35_44)
        val daily45_54Stat = Stat("45 a 54 años (por día)", INDEX_DAILY_45_54)
        val total45_54Stat = Stat("45 a 54 años (acumulado)", INDEX_TOTAL_45_54)
        val daily55_64Stat = Stat("55 a 64 años (por día)", INDEX_DAILY_55_64)
        val total55_64Stat = Stat("55 a 64 años (acumulado)", INDEX_TOTAL_55_64)
        val daily65_74Stat = Stat("65 a 74 años (por día)", INDEX_DAILY_65_74)
        val total65_74Stat = Stat("65 a 74 años (acumulado)", INDEX_TOTAL_65_74)
        val daily75_115Stat = Stat("75 a 115 años (por día)", INDEX_DAILY_75_115)
        val total75_115Stat = Stat("75 a 115 años (acumulado)", INDEX_TOTAL_75_115)
        val dailyUndefinedStat = Stat("Indefinidos (por día)", INDEX_DAILY_UNDEFINED)
        val totalUndefinedStat = Stat("Indefinidos (acumulado)", INDEX_TOTAL_UNDEFINED)
        val daily18_49Stat = Stat("18 a 49 años (por día)", INDEX_DAILY_18_49)
        val total18_49Stat = Stat("18 a 49 años (acumulado)", INDEX_TOTAL_18_49)
        val daily50_70Stat = Stat("50 a 70 años (por día)", INDEX_DAILY_50_70)
        val total50_70Stat = Stat("50 a 70 años (acumulado)", INDEX_TOTAL_50_70)
        val daily71_79Stat = Stat("71 a 79 años (por día)", INDEX_DAILY_71_79)
        val total71_79Stat = Stat("71 a 79 años (acumulado)", INDEX_TOTAL_71_79)
        val daily80_115Stat = Stat("80 a 115 años (por día)", INDEX_DAILY_80_115)
        val total80_115Stat = Stat("80 a 115 años (acumulado)", INDEX_TOTAL_80_115)
    }
}
