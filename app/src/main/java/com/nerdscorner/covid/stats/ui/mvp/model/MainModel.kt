package com.nerdscorner.covid.stats.ui.mvp.model

import com.nerdscorner.covid.stats.domain.*
import com.nerdscorner.covid.stats.networking.ServiceGenerator
import com.nerdscorner.covid.stats.networking.StatsService
import com.nerdscorner.covid.stats.utils.SharedPreferencesUtils
import com.nerdscorner.events.coroutines.extensions.withResult
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
        fetchStat(0, statsService::getStatsByCity, CitiesData.getInstance())
        fetchStat(1, statsService::getCtiStats, CtiData.getInstance())
        fetchStat(2, statsService::getDeceases, DeceasesData.getInstance())
        fetchStat(3, statsService::getGeneralStats, GeneralStatsData.getInstance())
        fetchStat(4, statsService::getP7StatisticsByCity, P7ByCityData.getInstance())
        fetchStat(5, statsService::getP7Statistics, P7Data.getInstance())
        fetchStat(6, statsService::getMobilityStats, MobilityData.getInstance())
    }

    private fun fetchStat(key: Int, call: KSuspendFunction0<String>, dataObject: DataObject) {
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

    fun setLastUpdateDateTime() {
        val dateTime = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault()).format(Date())
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

    companion object {
        var hasData = false
    }
}
