package com.nerdscorner.guiad.stats.ui.mvp.presenter

import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import com.nerdscorner.guiad.stats.ui.mvp.model.CreditsModel
import com.nerdscorner.guiad.stats.ui.mvp.view.CreditsView
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import org.greenrobot.eventbus.Subscribe


class CreditsPresenter(view: CreditsView, model: CreditsModel) : BaseActivityPresenter<CreditsView, CreditsModel>(view, model) {

    @Subscribe
    fun onGuiadViewMoreButtonClicked(event: CreditsView.GuiadViewMoreButtonClickedEvent) {
        launchOnBrowser(model.getGuiadUrl())
    }

    @Subscribe
    fun onDavidGiordanoViewMoreButtonClicked(event: CreditsView.DavidGiordanoViewMoreButtonClickedEvent) {
        launchOnBrowser(model.getDavidGiordanoUrl())
    }

    private fun launchOnBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        view.activity?.startActivity(browserIntent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                view.activity?.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
