package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.covid.stats.ui.fragment.MessageDialogFragment
import com.nerdscorner.covid.stats.ui.mvp.model.MessageDialogModel
import com.nerdscorner.covid.stats.ui.mvp.view.MessageDialogView
import com.nerdscorner.mvplib.events.bus.Bus
import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import org.greenrobot.eventbus.Subscribe

class MessageDialogPresenter(view: MessageDialogView, model: MessageDialogModel, bus: Bus) :
    BaseFragmentPresenter<MessageDialogView, MessageDialogModel>(view, model, bus) {

    fun setConfiguration(message: String?) {
        view.showTickImage()
        if (message == null) {
            view.hideMessage()
        } else {
            view.setMessage(message)
            view.showMessage()
        }
    }
    
    @Subscribe
    fun onActionButtonClicked(event: MessageDialogView.ActionButtonClickedEvent) {
        (view.fragment as? MessageDialogFragment)?.onActionButtonClicked()
    }
}
