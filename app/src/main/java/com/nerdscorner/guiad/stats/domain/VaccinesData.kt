package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.utils.DateUtils
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils

class VaccinesData private constructor() : DataObject() {

    override fun setData(data: String?) {
        super.setData(data?.replace(csvStringLiteralsRegex, EMPTY_STRING))
        dataLines.forEach { dataTokens ->
            dataTokens[INDEX_VACCINE] = EMPTY_STRING
            dataTokens[INDEX_DATE] = DateUtils.convertUsDateToUyDate(dataTokens[INDEX_DATE])
            dataTokens[INDEX_DAILY_PROGRESS] = dataTokens[INDEX_DAILY_PROGRESS].replace("%", EMPTY_STRING)
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
        totalVaccinationsStat,
        totalPeopleVaccinatedStat,
        totalPeopleFullyVaccinatedStat,
        dailyPeopleVaccinatedStat,
        dailyCoronavacStat,
        totalCoronavacStat,
        dailyPeopleCoronavacStat,
        dailyPeopleFullyCoronavacStat,
        dailyPfizerStat,
        totalPfizerStat,
        dailyPeoplePfizerStat,
        dailyPeopleFullyPfizerStat,
        dailyAstrazenecaStat,
        totalAstrazenecaStat,
        dailyPeopleAstrazenecaStat,
        dailyPeopleFullyAstrazenecaStat,
        dailyAgendaStat,
        totalProgressStat
    )

    override fun persist(data: String?) {
        SharedPreferencesUtils.saveVaccinesGlobalData(data)
    }

    companion object {
        private val csvStringLiteralsRegex = "\".*\"".toRegex()
        private val instance = VaccinesData()

        fun getInstance() = instance

        private const val INDEX_DATE = 1
        private const val INDEX_VACCINE = 2
        private const val INDEX_TOTAL_VACCINATIONS = 4
        private const val INDEX_TOTAL_PEOPLE_VACCINATED = 5
        private const val INDEX_TOTAL_PEOPLE_FULLY_VACCINATED = 6
        private const val INDEX_DAILY_PEOPLE_VACCINATED = 7
        private const val INDEX_DAILY_CORONAVAC = 8
        private const val INDEX_TOTAL_CORONAVAC = 9
        private const val INDEX_PEOPLE_CORONAVAC = 10
        private const val INDEX_PEOPLE_FULLY_CORONAVAC = 11
        private const val INDEX_DAILY_PFIZER = 12
        private const val INDEX_TOTAL_PFIZER = 13
        private const val INDEX_PEOPLE_PFIZER = 14
        private const val INDEX_PEOPLE_FULLY_PFIZER = 15
        private const val INDEX_DAILY_ASTRAZENECA = 16
        private const val INDEX_TOTAL_ASTRAZENECA = 17
        private const val INDEX_PEOPLE_ASTRAZENECA = 18
        private const val INDEX_PEOPLE_FULLY_ASTRAZENECA = 19
        private const val INDEX_DAILY_AGENDA = 22
        private const val INDEX_DAILY_PROGRESS = 24

        val totalVaccinationsStat = Stat("Vacunaciones (acumulado)", INDEX_TOTAL_VACCINATIONS)
        val totalPeopleVaccinatedStat = Stat("Total vacunados (acumulado)", INDEX_TOTAL_PEOPLE_VACCINATED)
        val totalPeopleFullyVaccinatedStat = Stat("Completamente vacunados (acumulado)", INDEX_TOTAL_PEOPLE_FULLY_VACCINATED)
        val dailyPeopleVaccinatedStat = Stat("Vacunados (por día)", INDEX_DAILY_PEOPLE_VACCINATED)

        val dailyCoronavacStat = Stat("Vacunaciones Coronavac (por día)", INDEX_DAILY_CORONAVAC)
        val totalCoronavacStat = Stat("Vacunaciones Coronavac (acumulado)", INDEX_TOTAL_CORONAVAC)
        val dailyPeopleCoronavacStat = Stat("Vacunados Coronavac (por día)", INDEX_PEOPLE_CORONAVAC)
        val dailyPeopleFullyCoronavacStat = Stat("Completamente vacunados Coronavac (por día)", INDEX_PEOPLE_FULLY_CORONAVAC)

        val dailyPfizerStat = Stat("Vacunaciones Pfizer (por día)", INDEX_DAILY_PFIZER)
        val totalPfizerStat = Stat("Vacunaciones Pfizer (acumulado)", INDEX_TOTAL_PFIZER)
        val dailyPeoplePfizerStat = Stat("Vacunados Pfizer (por día)", INDEX_PEOPLE_PFIZER)
        val dailyPeopleFullyPfizerStat = Stat("Completamente vacunados Pfizer (por día)", INDEX_PEOPLE_FULLY_PFIZER)

        val dailyAstrazenecaStat = Stat("Vacunaciones Astrazeneca (por día)", INDEX_DAILY_ASTRAZENECA)
        val totalAstrazenecaStat = Stat("Vacunaciones Astrazeneca (acumulado)", INDEX_TOTAL_ASTRAZENECA)
        val dailyPeopleAstrazenecaStat = Stat("Vacunados Astrazeneca (por día)", INDEX_PEOPLE_ASTRAZENECA)
        val dailyPeopleFullyAstrazenecaStat = Stat("Completamente vacunados Astrazeneca (por día)", INDEX_PEOPLE_FULLY_ASTRAZENECA)

        val dailyAgendaStat = Stat("Agendados (por día)", INDEX_DAILY_AGENDA)
        val totalProgressStat = Stat("Progreso de vacunación (% por día)", INDEX_DAILY_PROGRESS)
    }
}
