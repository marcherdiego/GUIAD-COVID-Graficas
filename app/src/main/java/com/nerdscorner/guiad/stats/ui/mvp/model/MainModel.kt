package com.nerdscorner.guiad.stats.ui.mvp.model

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.domain.*
import com.nerdscorner.guiad.stats.networking.ServiceGenerator
import com.nerdscorner.guiad.stats.networking.GuiadStatsService
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils
import com.nerdscorner.guiad.stats.extensions.withResult
import com.nerdscorner.guiad.stats.networking.VaccinesService
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.guiad.stats.utils.DateUtils
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KSuspendFunction0

class MainModel : BaseEventsModel() {
    private val baseColorIndex = 5
    private val graphColor = ColorUtils.getColor(baseColorIndex)
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
        fetchStat(vaccinesService::getCountryData, VaccinesByCityData.getInstance(), mandatoryLoad = false)
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

    fun buildCtiStatChart(resultFunc: (dataSet: ILineDataSet?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit) {
        buildStatChart(
            CtiData.getInstance(),
            CtiData.patientsQuantityStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getLineDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = resultFunc
        )
    }

    fun buildCitiesDataChart(resultFunc: (dataSet: List<ILineDataSet>?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit) {
        buildStatChart(
            CitiesData.getInstance(),
            CitiesData.inCourseStat,
            dataSetFunc = { dataObject, stat ->
                getAllCities()
                    .drop(1)
                    .mapIndexed { index, city ->
                        val chartColor = ColorUtils.getColor(baseColorIndex + index)
                        dataObject.getLineDataSet(stat, listOf(city), chartColor, chartColor, HOME_CHARTS_DATA_LIMIT)
                    }
            },
            resultFunc = resultFunc
        )
    }

    fun buildGeneralsDaraChart(resultFunc: (dataSet: ILineDataSet?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit) {
        buildStatChart(
            GeneralStatsData.getInstance(),
            GeneralStatsData.inCourseStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getLineDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = resultFunc
        )
    }

    fun buildDeceasesDataChart(resultFunc: (dataSet: ILineDataSet?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit) {
        buildStatChart(
            GeneralStatsData.getInstance(),
            GeneralStatsData.newDeceasesStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getLineDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = resultFunc
        )
    }

    fun buildP7DataChart(resultFunc: (dataSet: ILineDataSet?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit) {
        buildStatChart(
            P7Data.getInstance(),
            P7Data.p7Stat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getLineDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = resultFunc
        )
    }

    fun buildMobilityDataChart(resultFunc: (dataSet: ILineDataSet?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit) {
        buildStatChart(
            MobilityData.getInstance(),
            MobilityData.mobilityIndexStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getLineDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = resultFunc
        )
    }

    fun buildRawDataChart(resultFunc: (dataSet: ILineDataSet?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit) {
        buildStatChart(
            GeneralStatsData.getInstance(),
            GeneralStatsData.positivityStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getLineDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = resultFunc
        )
    }

    fun buildVaccinesBySegmentStatChart(resultFunc: (dataSet: ILineDataSet?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit) {
        buildStatChart(
            VaccinesBySegmentData.getInstance(),
            VaccinesBySegmentData.dailyNoRiskStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getLineDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = resultFunc
        )
    }

    fun buildVaccinesByAgeStatChart(resultFunc: (dataSet: ILineDataSet?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit) {
        buildStatChart(
            VaccinesByAgeData.getInstance(),
            VaccinesByAgeData.daily18_49Stat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getLineDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = resultFunc
        )
    }

    fun buildVaccinesGlobalStatChart(resultFunc: (dataSet: ILineDataSet?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit) {
        buildStatChart(
            VaccinesData.getInstance(),
            VaccinesData.totalProgressStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getLineDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = resultFunc
        )
    }

    fun buildVaccinesByCityStatChart(resultFunc: (dataSet: ILineDataSet?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit) {
        buildStatChart(
            VaccinesData.getInstance(),
            VaccinesData.dailyPeopleVaccinatedStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getLineDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = resultFunc
        )
    }

    private fun <T, D : DataObject> buildStatChart(
        dataObject: D,
        stat: Stat,
        dataSetFunc: (D, Stat) -> T,
        resultFunc: (dataSet: T?, statName: String, lastValue: String, isTrendingUp: Boolean?) -> Unit
    ) {
        resultFunc(null, stat.name, DataObject.N_A, null)
        CoroutineScope(Dispatchers.Main).launch {
            val (result, latestValue, isTrendingUp) = withContext(Dispatchers.Default) {
                Triple(
                    dataSetFunc(dataObject, stat),
                    dataObject.getLatestValue(stat),
                    dataObject.isTrendingUp(stat)
                )
            }
            resultFunc(result, stat.name, latestValue, isTrendingUp)
        }
    }

    class StatsFetchedSuccessfullyEvent
    class StatsFetchFailedEvent

    companion object {
        private const val HOME_CHARTS_DATA_LIMIT = 30
    }
}
