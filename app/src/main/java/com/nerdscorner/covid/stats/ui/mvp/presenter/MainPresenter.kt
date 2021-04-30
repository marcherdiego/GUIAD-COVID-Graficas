package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.covid.stats.ui.activities.GeneralStatsActivity
import com.nerdscorner.covid.stats.ui.activities.StatsByCityActivity
import com.nerdscorner.covid.stats.ui.activities.StatsCtiActivity
import com.nerdscorner.covid.stats.ui.fragment.MessageDialogFragment
import com.nerdscorner.covid.stats.ui.fragment.ProgressDialogFragment
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter

import com.nerdscorner.covid.stats.ui.mvp.model.MainModel
import com.nerdscorner.covid.stats.ui.mvp.view.MainView
import org.greenrobot.eventbus.Subscribe

class MainPresenter(view: MainView, model: MainModel) : BaseActivityPresenter<MainView, MainModel>(view, model) {

    private var progressDialog: ProgressDialogFragment? = null

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
    fun onStatsFetchedSuccessfully(event: MainModel.StatsFetchedSuccessfullyEvent) {
        hideLoadingState()
    }

    @Subscribe
    fun onStatsStatsFetchedFailed(event: MainModel.StatsFetchedFailedEvent) {
        hideLoadingState()
        showErrorState()
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
        view.withFragmentManager {
            MessageDialogFragment.showProgressDialog(
                fragmentManager = this,
                text = "Error al actualizar los datos",
                cancelable = true,
                actionCallback = {
                    showLoadingState()
                    model.fetchStats()
                }
            )
        }
    }

    private fun hideLoadingState() {
        progressDialog?.dismiss()
    }

    override fun onResume() {
        super.onResume()
        showLoadingState()
        model.fetchStats()
    }
}
