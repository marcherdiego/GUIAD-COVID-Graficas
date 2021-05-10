package com.nerdscorner.guiad.stats.ui.mvp.model

import com.nerdscorner.guiad.stats.domain.*
import com.nerdscorner.guiad.stats.networking.ServiceGenerator
import com.nerdscorner.guiad.stats.networking.GuiadStatsService
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils
import com.nerdscorner.events.coroutines.extensions.withResult
import com.nerdscorner.guiad.stats.networking.VaccinesService
import com.nerdscorner.guiad.stats.utils.DateUtils
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KSuspendFunction0

class MainModel : BaseEventsModel() {
    private val guiadStatsService = ServiceGenerator.createGuiadService(GuiadStatsService::class.java)
    private val vaccinesService = ServiceGenerator.createVaccinesService(VaccinesService::class.java)
    private val jobs = mutableMapOf<Int, Job>()

    fun fetchStats() {
        cancelPendingJobs()
        fetchStat(guiadStatsService::getStatsByCity, CitiesData.getInstance())
        fetchStat(guiadStatsService::getCtiStats, CtiData.getInstance())
        fetchStat(guiadStatsService::getDeceases, DeceasesData.getInstance())
        fetchStat(guiadStatsService::getGeneralStats, GeneralStatsData.getInstance())
        fetchStat(guiadStatsService::getP7StatisticsByCity, P7ByCityData.getInstance())
        fetchStat(guiadStatsService::getP7Statistics, P7Data.getInstance())
        fetchStat(guiadStatsService::getMobilityStats, MobilityData.getInstance())

        fetchStat(vaccinesService::getDataBySegment, VaccinesBySegmentData.getInstance(), mandatoryLoad = false)
        fetchStat(vaccinesService::getDataByAge, VaccinesByAgeData.getInstance(), mandatoryLoad = false)
        fetchStat(vaccinesService::getCountryData, VaccinesData.getInstance(), mandatoryLoad = false)
    }

    private fun fetchStat(call: KSuspendFunction0<String>, dataObject: DataObject, mandatoryLoad: Boolean = true) {
        val key = dataObject.hashCode()
        jobs[key] = withResult(
            resultFunc = call,
            success = {
                this?.trim()?.let {
                    dataObject.setData(it)
                    removePendingJob(key)
                } ?: run {
                    if (mandatoryLoad) {
                        bus.post(StatsFetchFailedEvent())
                    } else {
                        removePendingJob(key)
                    }
                }
            },
            fail = {
                printStackTrace()
                if (mandatoryLoad) {
                    bus.post(StatsFetchFailedEvent())
                } else {
                    removePendingJob(key)
                }
            }
        )
    }

    private fun removePendingJob(key: Int) {
        jobs.remove(key)
        if (jobs.isEmpty()) {
            bus.post(StatsFetchedSuccessfullyEvent())
        }
    }

    fun getLastUpdateDateTime() = SharedPreferencesUtils.getLastUpdateDateTime()

    fun hasNoDataYet() = getLastUpdateDateTime() == null

    fun setLastUpdateDateTime() {
        val dateTime = SimpleDateFormat(DateUtils.DATE_TIME_FORMAT, Locale.getDefault()).format(Date())
        SharedPreferencesUtils.saveLastUpdateDateTime(dateTime)
    }

    fun setRotateDeviceDialogShown() {
        SharedPreferencesUtils.setRotateDeviceDialogShown(true)
    }

    fun shouldShowRotateDeviceDialog() = SharedPreferencesUtils.getRotateDeviceDialogShown().not()

    fun getAllCities() = CitiesData.getAllCities()

    fun cancelPendingJobs() {
        synchronized(jobs) {
            jobs.values.forEach {
                it.cancel()
            }
            jobs.clear()
        }
    }

    class StatsFetchedSuccessfullyEvent
    class StatsFetchFailedEvent
}
