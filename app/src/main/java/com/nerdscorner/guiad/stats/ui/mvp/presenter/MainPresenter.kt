package com.nerdscorner.guiad.stats.ui.mvp.presenter

import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorInt
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.domain.*
import com.nerdscorner.guiad.stats.ui.activities.*
import com.nerdscorner.guiad.stats.ui.fragment.MessageDialogFragment
import com.nerdscorner.guiad.stats.ui.fragment.ProgressDialogFragment
import com.nerdscorner.guiad.stats.ui.fragment.RotateDeviceDialogFragment
import com.nerdscorner.guiad.stats.ui.mvp.model.MainModel
import com.nerdscorner.guiad.stats.ui.mvp.view.MainView
import com.nerdscorner.guiad.stats.utils.ColorUtils
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
        if (model.hasNoDataYet()) {
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

    private fun buildCtiStatChart(@ColorInt graphColor: Int) {
        buildStatChart(
            CtiData.getInstance(),
            CtiData.patientsQuantityStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = { dataSet, statName, lastValue, isTrendingUp ->
                view.setupCtiCard(dataSet, statName, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildCitiesDataChart(baseColorIndex: Int) {
        buildStatChart(
            CitiesData.getInstance(),
            CitiesData.inCourseStat,
            dataSetFunc = { dataObject, stat ->
                model
                    .getAllCities()
                    .drop(1)
                    .mapIndexed { index, city ->
                        val chartColor = ColorUtils.getColor(baseColorIndex + index)
                        dataObject.getDataSet(stat, listOf(city), chartColor, chartColor, HOME_CHARTS_DATA_LIMIT)
                    }
            },
            resultFunc = { dataSet, statName, lastValue, isTrendingUp ->
                view.setupCitiesCard(dataSet, statName, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildGeneralsDaraChart(@ColorInt graphColor: Int) {
        buildStatChart(
            GeneralStatsData.getInstance(),
            GeneralStatsData.inCourseStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = { dataSet, statName, lastValue, isTrendingUp ->
                view.setupGeneralsCard(dataSet, statName, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildDeceasesDataChart(@ColorInt graphColor: Int) {
        buildStatChart(
            GeneralStatsData.getInstance(),
            GeneralStatsData.newDeceasesStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = { dataSet, statName, lastValue, isTrendingUp ->
                view.setupDeceasesCard(dataSet, statName, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildP7DataChart(@ColorInt graphColor: Int) {
        buildStatChart(
            P7Data.getInstance(),
            P7Data.p7Stat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = { dataSet, statName, lastValue, isTrendingUp ->
                view.setupP7Card(dataSet, statName, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildMobilityDataChart(@ColorInt graphColor: Int) {
        buildStatChart(
            MobilityData.getInstance(),
            MobilityData.mobilityIndexStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = { dataSet, statName, lastValue, isTrendingUp ->
                view.setupMobilityCard(dataSet, statName, lastValue, isTrendingUp)
            }
        )
    }

    private fun buildRawDataChart(@ColorInt graphColor: Int) {
        buildStatChart(
            GeneralStatsData.getInstance(),
            GeneralStatsData.positivityStat,
            dataSetFunc = { dataObject, stat ->
                dataObject.getDataSet(stat, graphColor, graphColor, HOME_CHARTS_DATA_LIMIT)
            },
            resultFunc = { dataSet, statName, lastValue, isTrendingUp ->
                view.setupRawDataCard(dataSet, statName, lastValue, isTrendingUp)
            }
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

    private fun showLoadingState() {
        view.withFragmentManager {
            progressDialog = ProgressDialogFragment.showProgressDialog(
                fragmentManager = this,
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
        triggerRefreshData(model.hasNoDataYet())
        if (model.shouldShowRotateDeviceDialog()) {
            view.withFragmentManager {
                RotateDeviceDialogFragment.show(this)
            }
            model.setRotateDeviceDialogShown()
        }
    }

    override fun onPause() {
        super.onPause()
        model.cancelPendingJobs()
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
