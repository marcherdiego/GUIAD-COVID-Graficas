package com.nerdscorner.covid.stats.ui.mvp.model

import com.nerdscorner.covid.stats.domain.CitiesData
import com.nerdscorner.covid.stats.domain.CtiData
import com.nerdscorner.covid.stats.domain.DeceasesData
import com.nerdscorner.covid.stats.domain.GeneralStatsData
import com.nerdscorner.covid.stats.exceptions.NetworkException
import com.nerdscorner.covid.stats.networking.ServiceGenerator
import com.nerdscorner.covid.stats.networking.StatsService
import com.nerdscorner.covid.stats.networking.enqueueResponseNotNull
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import retrofit2.Call

class MainModel : BaseEventsModel() {
    private val statsService = ServiceGenerator.createService(StatsService::class.java)
    private val enqueuedCalls = mutableListOf<Call<*>>()

    private val failedRequestCallback: (NetworkException) -> Unit = {
        it.throwable?.printStackTrace()
        bus.post(StatsFetchedFailedEvent())
    }

    fun fetchStats() {
        fetchCtiStats()
        fetchStatsByCity()
        fetchGeneralStats()
        fetchDeceasesStats()
    }

    private fun fetchCtiStats() {
        val call = statsService.getCtiStats()
        call.enqueueResponseNotNull(
            success = {
                CtiData.data = it
                removePendingCall(call)
            },
            fail = failedRequestCallback
        )
        enqueuedCalls.add(call)
    }

    private fun fetchStatsByCity() {
        val call = statsService.getStatsByCity()
        call.enqueueResponseNotNull(
            success = {
                CitiesData.data = it.trim()
                removePendingCall(call)
            },
            fail = failedRequestCallback
        )
        enqueuedCalls.add(call)
    }

    private fun fetchGeneralStats() {
        val call = statsService.getGeneralStats()
        call.enqueueResponseNotNull(
            success = {
                GeneralStatsData.data = it.trim()
                removePendingCall(call)
            },
            fail = failedRequestCallback
        )
        enqueuedCalls.add(call)
    }

    private fun fetchDeceasesStats() {
        val call = statsService.getDeceases()
        call.enqueueResponseNotNull(
            success = {
                DeceasesData.data = it.trim()
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

    class StatsFetchedSuccessfullyEvent
    class StatsFetchedFailedEvent
}
