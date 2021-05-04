package com.nerdscorner.covid.stats.ui.mvp.model

import com.nerdscorner.covid.stats.domain.*
import com.nerdscorner.covid.stats.exceptions.NetworkException
import com.nerdscorner.covid.stats.networking.ServiceGenerator
import com.nerdscorner.covid.stats.networking.StatsService
import com.nerdscorner.covid.stats.networking.enqueueResponseNotNull
import com.nerdscorner.covid.stats.utils.SharedPreferencesUtils
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class MainModel : BaseEventsModel() {
    private val statsService = ServiceGenerator.createService(StatsService::class.java)
    private val enqueuedCalls = mutableListOf<Call<*>>()
    private val failedRequestCallback: (NetworkException) -> Unit = {
        it.throwable?.printStackTrace()
        bus.post(StatsFetchedFailedEvent())
    }

    fun fetchStats() {
        enqueuedCalls.forEach {
            it.cancel()
        }
        enqueuedCalls.clear()
        fetchStat(statsService.getStatsByCity(), CitiesData.getInstance())
        fetchStat(statsService.getCtiStats(), CtiData.getInstance())
        fetchStat(statsService.getDeceases(), DeceasesData.getInstance())
        fetchStat(statsService.getGeneralStats(), GeneralStatsData.getInstance())
        fetchStat(statsService.getP7StatisticsByCity(), P7ByCityData.getInstance())
        fetchStat(statsService.getP7Statistics(), P7Data.getInstance())
        fetchStat(statsService.getMobilityStats(), MobilityData.getInstance())
    }

    private fun fetchStat(call: Call<String>, dataObject: DataObject) {
        call.enqueueResponseNotNull(
            success = {
                dataObject.setData(it.trim())
                removePendingCall(call)
            },
            fail = failedRequestCallback
        )
        enqueuedCalls.add(call)
    }

    private fun removePendingCall(call: Call<*>) {
        enqueuedCalls.remove(call)
        if (enqueuedCalls.isEmpty()) {
            bus.post(StatsFetchedSuccessfullyEvent())
        }
    }

    fun getLastUpdateDateTime() = SharedPreferencesUtils.getLastUpdateDateTime()

    fun setLastUpdateDateTime() {
        val dateTime = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault()).format(Date())
        SharedPreferencesUtils.saveLastUpdateDateTime(dateTime)
    }

    class StatsFetchedSuccessfullyEvent
    class StatsFetchedFailedEvent
    
    companion object {
        var hasData = false
    }
}
