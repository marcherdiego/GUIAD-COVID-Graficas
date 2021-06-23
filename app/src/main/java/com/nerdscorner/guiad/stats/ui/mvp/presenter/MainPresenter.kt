package com.nerdscorner.guiad.stats.ui.mvp.presenter

import android.view.Menu
import android.view.MenuItem
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.ui.activities.*
import com.nerdscorner.guiad.stats.ui.fragment.MessageDialogFragment
import com.nerdscorner.guiad.stats.ui.fragment.RotateDeviceDialogFragment
import com.nerdscorner.guiad.stats.ui.mvp.model.MainModel
import com.nerdscorner.guiad.stats.ui.mvp.view.MainView
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import org.greenrobot.eventbus.Subscribe

class MainPresenter(view: MainView, model: MainModel) : BaseActivityPresenter<MainView, MainModel>(view, model) {

    private var refreshing = false
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
    fun onVaccinesBySegmentStatsButtonClicked(event: MainView.VaccinesBySegmentStatsButtonClickedEvent) {
        startActivity(StatsVaccineBySegmentActivity::class.java)
    }

    @Subscribe
    fun onVaccinesByAgeStatsButtonClicked(event: MainView.VaccinesByAgeStatsButtonClickedEvent) {
        startActivity(StatsVaccineByAgeActivity::class.java)
    }

    @Subscribe
    fun onVaccinesGlobalStatsButtonClicked(event: MainView.VaccinesGlobalStatsButtonClickedEvent) {
        startActivity(StatsVaccineGlobalActivity::class.java)
    }

    @Subscribe
    fun onVaccinesByCityStatsButtonClicked(event: MainView.VaccinesByCityStatsButtonClickedEvent) {
        startActivity(StatsVaccineByCityActivity::class.java)
    }

    @Subscribe
    fun onOnRefreshTriggered(event: MainView.OnRefreshTriggeredEvent) {
        triggerRefreshData(true)
    }

    @Subscribe
    fun onStatsFetchedSuccessfully(event: MainModel.StatsFetchedSuccessfullyEvent) {
        model.setLastUpdateDateTime()
        refreshWidgetsState()
        hideLoadingState()
    }

    @Subscribe
    fun onStatsFetchFailed(event: MainModel.StatsFetchFailedEvent) {
        hideLoadingState()
        showErrorState()
    }

    private fun refreshWidgetsState() {
        val lastUpdate = model.getLastUpdateDateTime() ?: "Nunca"
        view.setLastUpdateDate("Última actualización: $lastUpdate")
        if (model.hasNoDataYet()) {
            return
        }

        model.buildCtiStatChart { dataSet, statName, lastValue, lastUpdate, isTrendingUp ->
            view.setupCtiCard(dataSet, statName, lastValue, lastUpdate, isTrendingUp)
        }
        model.buildCitiesDataChart { dataSet, statName, lastValue, lastUpdate,  isTrendingUp ->
            view.setupCitiesCard(dataSet, statName, lastValue, lastUpdate,  isTrendingUp)
        }
        model.buildGeneralsDaraChart { dataSet, statName, lastValue, lastUpdate,  isTrendingUp ->
            view.setupGeneralsCard(dataSet, statName, lastValue, lastUpdate,  isTrendingUp)
        }
        model.buildDeceasesDataChart { dataSet, statName, lastValue, lastUpdate,  isTrendingUp ->
            view.setupDeceasesCard(dataSet, statName, lastValue, lastUpdate,  isTrendingUp)
        }
        model.buildP7DataChart { dataSet, statName, lastValue, lastUpdate,  isTrendingUp ->
            view.setupP7Card(dataSet, statName, lastValue, lastUpdate,  isTrendingUp)
        }
        model.buildMobilityDataChart { dataSet, statName, lastValue, lastUpdate,  isTrendingUp ->
            view.setupMobilityCard(dataSet, statName, lastValue, lastUpdate,  isTrendingUp)
        }
        model.buildRawDataChart { dataSet, statName, lastValue, lastUpdate,  isTrendingUp ->
            view.setupRawDataCard(dataSet, statName, lastValue, lastUpdate,  isTrendingUp)
        }
        model.buildVaccinesBySegmentStatChart { dataSet, statName, lastValue, lastUpdate,  isTrendingUp ->
            view.setupVaccinesBySegmentCard(dataSet, statName, lastValue, lastUpdate,  isTrendingUp)
        }
        model.buildVaccinesByAgeStatChart { dataSet, statName, lastValue, lastUpdate,  isTrendingUp ->
            view.setupVaccinesByAgeCard(dataSet, statName, lastValue, lastUpdate,  isTrendingUp)
        }
        model.buildVaccinesGlobalStatChart { dataSet, statName, lastValue, lastUpdate,  isTrendingUp ->
            view.setupVaccinesGlobalStatsCard(dataSet, statName, "$lastValue%",lastUpdate, isTrendingUp)
        }
        model.buildVaccinesByCityStatChart { dataSet, statName, lastValue, lastUpdate,  isTrendingUp ->
            view.setupVaccinesByCityStatsCard(dataSet, statName, lastValue, lastUpdate,  isTrendingUp)
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
                    triggerRefreshData(true)
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

    private fun triggerRefreshData(shouldShowProgress: Boolean) {
        if (refreshing) {
            return
        }
        if (shouldShowProgress) {
            view.setRefreshing(true)
        }
        model.fetchStats()
    }

    private fun hideLoadingState() {
        view.setRefreshing(false)
        refreshing = false
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
        hideLoadingState()
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
}
