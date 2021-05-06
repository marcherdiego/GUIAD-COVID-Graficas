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

    private fun buildGeneralsDaraChart(@ColorInt graphColor: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val generalsData = GeneralStatsData.getInstance()
            val generalsStat = GeneralStatsData.inCourseStat
            val generalsDataSet = withContext(Dispatchers.Default) {
                generalsData.getDataSet(
                    generalsStat,
                    graphColor,
                    graphColor,
                    HOME_CHARTS_DATA_LIMIT
                )
            }
            view.setupGeneralsCard(
                generalsDataSet,
                generalsStat.name,
                generalsData.getLatestValue(generalsStat),
                generalsData.isTrendingUp(generalsStat)
            )
        }
    }

    private fun buildDeceasesDataChart(@ColorInt graphColor: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val deceasesData = GeneralStatsData.getInstance()
            val deceasesStat = GeneralStatsData.newDeceasesStat
            val deceasesDataSet = withContext(Dispatchers.Default) {
                deceasesData.getDataSet(
                    deceasesStat,
                    graphColor,
                    graphColor,
                    HOME_CHARTS_DATA_LIMIT
                )
            }
            view.setupDeceasesCard(
                deceasesDataSet,
                deceasesStat.name,
                deceasesData.getLatestValue(deceasesStat),
                deceasesData.isTrendingUp(deceasesStat)
            )
        }
    }

    private fun buildP7DataChart(@ColorInt graphColor: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val p7Data = P7Data.getInstance()
            val p7Stat = P7Data.p7Stat
            val p7DataSet = withContext(Dispatchers.Default) {
                p7Data.getDataSet(
                    p7Stat,
                    graphColor,
                    graphColor,
                    HOME_CHARTS_DATA_LIMIT
                )
            }
            view.setupP7Card(
                p7DataSet,
                p7Stat.name,
                p7Data.getLatestValue(p7Stat),
                p7Data.isTrendingUp(p7Stat)
            )
        }
    }

    private fun buildMobilityDataChart(@ColorInt graphColor: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val mobilityData = MobilityData.getInstance()
            val mobilityStat = MobilityData.mobilityIndexStat
            val mobilityDataSet = withContext(Dispatchers.Default) {
                mobilityData.getDataSet(
                    mobilityStat,
                    graphColor,
                    graphColor,
                    HOME_CHARTS_DATA_LIMIT
                )
            }
            view.setupMobilityCard(
                mobilityDataSet,
                mobilityStat.name,
                mobilityData.getLatestValue(mobilityStat),
                mobilityData.isTrendingUp(mobilityStat)
            )
        }
    }

    private fun buildRawDataChart(@ColorInt graphColor: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val rawData = GeneralStatsData.getInstance()
            val rawStat = GeneralStatsData.positivityStat
            val rawDataSet = withContext(Dispatchers.Default) {
                rawData.getDataSet(
                    rawStat,
                    graphColor,
                    graphColor,
                    HOME_CHARTS_DATA_LIMIT
                )
            }
            view.setupRawDataCard(
                rawDataSet,
                rawStat.name,
                rawData.getLatestValue(rawStat),
                rawData.isTrendingUp(rawStat)
            )
        }
    }

    private fun buildCitiesDataChart(baseColorIndex: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val citiesData = CitiesData.getInstance()
            val citiesStat = CitiesData.inCourseStat
            val citiesDataSets = withContext(Dispatchers.Default) {
                model
                    .getAllCities()
                    .drop(1)
                    .mapIndexed { index, city ->
                        val chartColor = ColorUtils.getColor(baseColorIndex + index)
                        citiesData.getDataSet(
                            citiesStat,
                            listOf(city),
                            chartColor,
                            chartColor,
                            HOME_CHARTS_DATA_LIMIT
                        )
                    }
            }
            // Cities data
            view.setupCitiesCard(
                citiesDataSets,
                citiesStat.name,
                citiesData.getLatestValue(citiesStat),
                citiesData.isTrendingUp(citiesStat)
            )
        }
    }

    private fun buildCtiStatChart(@ColorInt graphColor: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val ctiData = CtiData.getInstance()
            val ctiStat = CtiData.patientsQuantityStat
            val ctiDataSet = withContext(Dispatchers.Default) {
                ctiData.getDataSet(
                    ctiStat,
                    graphColor,
                    graphColor,
                    HOME_CHARTS_DATA_LIMIT
                )
            }
            // CTI data
            view.setupCtiCard(
                ctiDataSet,
                ctiStat.name,
                ctiData.getLatestValue(ctiStat),
                ctiData.isTrendingUp(ctiStat)
            )
        }
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
