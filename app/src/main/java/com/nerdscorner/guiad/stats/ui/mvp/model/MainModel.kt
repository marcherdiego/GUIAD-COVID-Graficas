package com.nerdscorner.guiad.stats.ui.mvp.model

import com.nerdscorner.guiad.stats.domain.*
import com.nerdscorner.guiad.stats.networking.ServiceGenerator
import com.nerdscorner.guiad.stats.networking.StatsService
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils
import com.nerdscorner.events.coroutines.extensions.withResult
import com.nerdscorner.guiad.stats.utils.DateUtils
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KSuspendFunction0

class MainModel : BaseEventsModel() {
    private val statsService = ServiceGenerator.createService(StatsService::class.java)
    private val jobs = mutableMapOf<Int, Job>()

    fun fetchStats() {
        cancelPendingJobs()
        fetchStat(statsService::getStatsByCity, CitiesData.getInstance())
        fetchStat(statsService::getCtiStats, CtiData.getInstance())
        fetchStat(statsService::getDeceases, DeceasesData.getInstance())
        fetchStat(statsService::getGeneralStats, GeneralStatsData.getInstance())
        fetchStat(statsService::getP7StatisticsByCity, P7ByCityData.getInstance())
        fetchStat(statsService::getP7Statistics, P7Data.getInstance())
        fetchStat(statsService::getMobilityStats, MobilityData.getInstance())
    }

    private fun fetchStat(call: KSuspendFunction0<String>, dataObject: DataObject) {
        val key = dataObject.hashCode()
        jobs[key] = withResult(
            resultFunc = call,
            success = {
                dataObject.setData(this!!.trim())
                removePendingJob(key)
            },
            fail = {
                printStackTrace()
                bus.post(StatsFetchedFailedEvent())
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
    class StatsFetchedFailedEvent
}
