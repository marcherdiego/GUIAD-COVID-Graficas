package com.nerdscorner.covid.stats.ui.mvp.presenter

import android.view.Menu
import android.view.MenuItem
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.activities.*
import com.nerdscorner.covid.stats.ui.fragment.MessageDialogFragment
import com.nerdscorner.covid.stats.ui.fragment.ProgressDialogFragment
import com.nerdscorner.covid.stats.ui.mvp.model.MainModel
import com.nerdscorner.covid.stats.ui.mvp.view.MainView
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import org.greenrobot.eventbus.Subscribe

class MainPresenter(view: MainView, model: MainModel) : BaseActivityPresenter<MainView, MainModel>(view, model) {

    private var progressDialog: ProgressDialogFragment? = null
    private var errorDialog: MessageDialogFragment? = null

    init {
        refreshLastUpdateTime()
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
    fun onStatsFetchedSuccessfully(event: MainModel.StatsFetchedSuccessfullyEvent) {
        MainModel.hasData = true
        model.setLastUpdateDateTime()
        refreshLastUpdateTime()
        hideLoadingState()
    }

    @Subscribe
    fun onStatsStatsFetchedFailed(event: MainModel.StatsFetchedFailedEvent) {
        hideLoadingState()
        showErrorState()
    }

    @Subscribe
    fun onCreditsButtonClicked(event: MainView.CreditsButtonClickedEvent) {
        startActivity(CreditsActivity::class.java)
    }

    private fun refreshLastUpdateTime() {
        val lastUpdate = model.getLastUpdateDateTime() ?: "Nunca"
        view.setLastUpdateDate("Última actualización: $lastUpdate")
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
        }
        return true
    }
}
