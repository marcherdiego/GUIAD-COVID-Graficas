package com.nerdscorner.covid.stats.ui.mvp.presenter

import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorInt
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.domain.*
import com.nerdscorner.covid.stats.ui.activities.*
import com.nerdscorner.covid.stats.ui.fragment.MessageDialogFragment
import com.nerdscorner.covid.stats.ui.fragment.ProgressDialogFragment
import com.nerdscorner.covid.stats.ui.fragment.RotateDeviceDialogFragment
import com.nerdscorner.covid.stats.ui.mvp.model.MainModel
import com.nerdscorner.covid.stats.ui.mvp.view.MainView
import com.nerdscorner.covid.stats.utils.ColorUtils
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import kotlinx.coroutines.*
import org.greenrobot.eventbus.Subscribe

class MainPresenter(view: MainView, model: MainModel) : BaseActivityPresenter<MainView, MainModel>(view, model) {

    private var progressDialog: ProgressDialogFragment? = null
    private var errorDialog: MessageDialogFragment? = null

    init {
        refreshWidgetsState()
    }

    @Subscribe
    fun onCtiButtonClicked(event: MainView.CtiButtonClickedEvent) {
        startActivity(StatsCtiActivity::class.java)
    }

    @Subscribe
    fun onCitiesButtonClicked(event: MainView.CitiesButtonClickedEvent) {
        startActivity(StatsByCityActivity::class.java)
    }

    @Subscribe
    fun onGeneralStatsButtonClicked(event: MainView.GeneralStatsButtonClickedEvent) {
        startActivity(GeneralStatsActivity::class.java)
    }

    @Subscribe
    fun onDeceasesStatsButtonClicked(event: MainView.DeceasesStatsButtonClickedEvent) {
        startActivity(DeceasesStatsActivity::class.java)
    }

    @Subscribe
    fun onP7StatsButtonClicked(event: MainView.P7StatsButtonClickedEvent) {
        startActivity(P7StatsActivity::class.java)
    }

    @Subscribe
    fun onMobilityStatsButtonClicked(event: MainView.MobilityStatsButtonClickedEvent) {
        startActivity(MobilityActivity::class.java)
    }

    @Subscribe
    fun onRawDataGeneralStatsButtonClicked(event: MainView.RawDataGeneralStatsButtonClickedEvent) {
        startActivity(RawDataGeneralStatsActivity::class.java)
    }

    @Subscribe
    fun onStatsFetchedSuccessfully(event: MainModel.StatsFetchedSuccessfullyEvent) {
        MainModel.hasData = true
        model.setLastUpdateDateTime()
        refreshWidgetsState()
        hideLoadingState()
    }

    @Subscribe
    fun onStatsStatsFetchedFailed(event: MainModel.StatsFetchedFailedEvent) {
        hideLoadingState()
        showErrorState()
    }

    private fun refreshWidgetsState() {
        val lastUpdate = model.getLastUpdateDateTime() ?: "Nunca"
        view.setLastUpdateDate("Última actualización: $lastUpdate")
        if (MainModel.hasData.not()) {
            return
        }

        val baseColorIndex = 5
        val graphColor = ColorUtils.getColor(baseColorIndex)
        buildCtiStatChart(graphColor)
        buildCitiesDataChart(baseColorIndex)
        buildGeneralsDaraChart(graphColor)
        buildDeceasesDataChart(graphColor)
        buildP7DataChart(graphColor)
        buildMobilityDataChart(graphColor)
        buildRawDataChart(graphColor)
    }

    private fun <T> buildStatChart(
        dataSetFunc: () -> Triple<T, String, Boolean>,
        resultFunc: (dataSet: T, lastValue: String, isTrendingUp: Boolean) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.Default) {
                dataSetFunc()
            }
            resultFunc(result.first, result.second, result.third)
        }
    }

    private fun buildCtiStatChart(@ColorInt graphColor: Int) {
        val ctiData = CtiData.getInstance()
        val ctiStat = CtiData.patientsQuantityStat
        buildStatChart(
            dataSetFunc = {
                Triple(
                    ctiData.getDataSet(ctiStat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT),
                    ctiData.getLatestValue(ctiStat),
                    ctiData.isTrendingUp(ctiStat)
                )
            },
            resultFunc = { dataSet, lastValue, isTrendingUp ->
                view.setupCtiCard(dataSet, ctiStat.name, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildCitiesDataChart(baseColorIndex: Int) {
        val citiesData = CitiesData.getInstance()
        val citiesStat = CitiesData.inCourseStat
        buildStatChart(
            dataSetFunc = {
                Triple(
                    model
                        .getAllCities()
                        .drop(1)
                        .mapIndexed { index, city ->
                            val chartColor = ColorUtils.getColor(baseColorIndex + index)
                            citiesData.getDataSet(citiesStat, listOf(city), chartColor, chartColor, HOME_CHARTS_DATA_LIMIT)
                        },
                    citiesData.getLatestValue(citiesStat),
                    citiesData.isTrendingUp(citiesStat)
                )
            },
            resultFunc = { dataSet, lastValue, isTrendingUp ->
                view.setupCitiesCard(dataSet, citiesStat.name, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildGeneralsDaraChart(@ColorInt graphColor: Int) {
        val generalsData = GeneralStatsData.getInstance()
        val generalsStat = GeneralStatsData.inCourseStat
        buildStatChart(
            dataSetFunc = {
                Triple(
                    generalsData.getDataSet(generalsStat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT),
                    generalsData.getLatestValue(generalsStat),
                    generalsData.isTrendingUp(generalsStat)
                )
            },
            resultFunc = { dataSet, lastValue, isTrendingUp ->
                view.setupGeneralsCard(dataSet, generalsStat.name, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildDeceasesDataChart(@ColorInt graphColor: Int) {
        val deceasesData = GeneralStatsData.getInstance()
        val deceasesStat = GeneralStatsData.newDeceasesStat
        buildStatChart(
            dataSetFunc = {
                Triple(
                    deceasesData.getDataSet(deceasesStat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT),
                    deceasesData.getLatestValue(deceasesStat),
                    deceasesData.isTrendingUp(deceasesStat)
                )
            },
            resultFunc = { dataSet, lastValue, isTrendingUp ->
                view.setupDeceasesCard(dataSet, deceasesStat.name, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildP7DataChart(@ColorInt graphColor: Int) {
        val p7Data = P7Data.getInstance()
        val p7Stat = P7Data.p7Stat
        buildStatChart(
            dataSetFunc = {
                Triple(
                    p7Data.getDataSet(p7Stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT),
                    p7Data.getLatestValue(p7Stat),
                    p7Data.isTrendingUp(p7Stat)
                )
            },
            resultFunc = { dataSet, lastValue, isTrendingUp ->
                view.setupP7Card(dataSet, p7Stat.name, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildMobilityDataChart(@ColorInt graphColor: Int) {
        val mobilityData = MobilityData.getInstance()
        val mobilityStat = MobilityData.mobilityIndexStat
        buildStatChart(
            dataSetFunc = {
                Triple(
                    mobilityData.getDataSet(mobilityStat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT),
                    mobilityData.getLatestValue(mobilityStat),
                    mobilityData.isTrendingUp(mobilityStat)
                )
            },
            resultFunc = { dataSet, lastValue, isTrendingUp ->
                view.setupMobilityCard(dataSet, mobilityStat.name, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildRawDataChart(@ColorInt graphColor: Int) {
        val rawData = GeneralStatsData.getInstance()
        val rawStat = GeneralStatsData.positivityStat
        buildStatChart(
            dataSetFunc = {
                Triple(
                    rawData.getDataSet(rawStat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT),
                    rawData.getLatestValue(rawStat),
                    rawData.isTrendingUp(rawStat)
                )
            },
            resultFunc = { dataSet, lastValue, isTrendingUp ->
                view.setupRawDataCard(dataSet, rawStat.name, lastValue, isTrendingUp)
            }
        )
    }

    private fun showLoadingState() {
        view.withFragmentManager {
            progressDialog = ProgressDialogFragment.showProgressDialog(
                fragmentManager = this,
                showProgress = true,
                text = "Actualizando datos...",
                cancelable = false
            )
        }
    }

    private fun showErrorState() {
        if (errorDialog != null) {
            return
        }
        view.withFragmentManager {
            errorDialog = MessageDialogFragment.showProgressDialog(
                fragmentManager = this,
                text = "Error al actualizar los datos",
                cancelable = true,
                primaryActionCallback = {
                    showLoadingState()
                    model.fetchStats()
                },
                secondaryActionCallback = if (model.getLastUpdateDateTime() == null) {
                    null
                } else {
                    {}
                },
                dismissListener = {
                    errorDialog = null
                }
            )
        }
    }

    private fun hideLoadingState() {
        progressDialog?.dismiss()
    }

    private fun triggerRefreshData(shouldShowProgress: Boolean) {
        if (shouldShowProgress) {
            showLoadingState()
        }
        model.fetchStats()
    }

    override fun onResume() {
        super.onResume()
        triggerRefreshData(MainModel.hasData.not())
        if (model.shouldShowRotateDeviceDialog()) {
            view.withFragmentManager {
                RotateDeviceDialogFragment.show(this)
            }
            model.setRotateDeviceDialogShown()
        }
    }

    override fun onPause() {
        super.onPause()
        progressDialog?.dismiss()
        errorDialog?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        view.activity?.menuInflater?.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_refresh -> triggerRefreshData(true)
            R.id.action_credits -> startActivity(CreditsActivity::class.java)
        }
        return true
    }

    companion object {
        private const val HOME_CHARTS_DATA_LIMIT = 30
    }
}
