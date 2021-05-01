package com.nerdscorner.covid.stats.ui.mvp.presenter

import com.nerdscorner.covid.stats.ui.fragment.MessageDialogFragment
import com.nerdscorner.covid.stats.ui.mvp.model.MessageDialogModel
import com.nerdscorner.covid.stats.ui.mvp.view.MessageDialogView
import com.nerdscorner.mvplib.events.bus.Bus
import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import org.greenrobot.eventbus.Subscribe

class MessageDialogPresenter(view: MessageDialogView, model: MessageDialogModel, bus: Bus) :
    BaseFragmentPresenter<MessageDialogView, MessageDialogModel>(view, model, bus) {

    fun setConfiguration(message: String?, isPrimaryActionVisible: Boolean, isSecondaryActionVisible: Boolean) {
        view.showTickImage()
        if (message == null) {
            view.hideMessage()
        } else {
            view.setMessage(message)
            view.showMessage()
        }
        if (isPrimaryActionVisible) {
            view.showPrimaryActionButton()
        } else {
            view.hidePrimaryActionButton()
        }
        if (isSecondaryActionVisible) {
            view.showSecondaryActionButton()
        } else {
            view.hideSecondaryActionButton()
        }
    }

    @Subscribe
    fun onPrimaryActionButtonClicked(event: MessageDialogView.PrimaryActionButtonClickedEvent) {
        (view.fragment as? MessageDialogFragment)?.onPrimaryActionButtonClicked()
    }

    @Subscribe
    fun onSecondaryActionButtonClicked(event: MessageDialogView.SecondaryActionButtonClickedEvent) {
        (view.fragment as? MessageDialogFragment)?.onSecondaryActionButtonClicked()
    }
}
