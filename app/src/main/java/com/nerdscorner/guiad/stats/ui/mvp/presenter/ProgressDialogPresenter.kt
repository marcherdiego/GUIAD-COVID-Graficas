package com.nerdscorner.guiad.stats.ui.mvp.presenter

import com.nerdscorner.guiad.stats.ui.mvp.model.ProgressDialogModel
import com.nerdscorner.guiad.stats.ui.mvp.view.ProgressDialogView
import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter

class ProgressDialogPresenter(view: ProgressDialogView, model: ProgressDialogModel) :
    BaseFragmentPresenter<ProgressDialogView, ProgressDialogModel>(view, model) {

    fun setConfiguration(progressbarIsVisible: Boolean, message: String?) {
        if (progressbarIsVisible) {
            view.showProgressBar()
        } else {
            view.showTickImage()
        }
        if (message == null) {
            view.hideMessage()
        } else {
            view.setMessage(message)
            view.showMessage()
        }
    }
}
