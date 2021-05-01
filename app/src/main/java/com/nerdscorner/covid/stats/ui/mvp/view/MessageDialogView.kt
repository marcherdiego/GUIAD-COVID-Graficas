package com.nerdscorner.covid.stats.ui.mvp.view

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.nerdscorner.covid.stats.R
import com.nerdscorner.mvplib.events.view.BaseFragmentView

class MessageDialogView(dialog: DialogFragment) : BaseFragmentView(dialog) {
    private val tickImageView: ImageView
    private val dialogText: TextView
    private val primaryActionButton: Button
    private val secondaryActionButton: Button

    init {
        with (dialog.requireView()) {
            tickImageView = findViewById(R.id.ui_imageview)
            dialogText = findViewById(R.id.text)
            primaryActionButton = findViewById(R.id.primary_action_button)
            secondaryActionButton = findViewById(R.id.secondary_action_button)
            onClick(primaryActionButton, PrimaryActionButtonClickedEvent())
            onClick(secondaryActionButton, SecondaryActionButtonClickedEvent())
        }
    }

    fun setMessage(message: String?) {
        dialogText.text = message
    }

    fun showProgressBar() {
        tickImageView.visibility = View.GONE
    }

    fun showTickImage() {
        tickImageView.visibility = View.VISIBLE
    }

    fun hideMessage() {
        dialogText.visibility = View.GONE
    }

    fun showMessage() {
        dialogText.visibility = View.VISIBLE
    }

    fun showPrimaryActionButton() {
        primaryActionButton.visibility = View.VISIBLE
    }

    fun hidePrimaryActionButton() {
        primaryActionButton.visibility = View.GONE
    }

    fun showSecondaryActionButton() {
        secondaryActionButton.visibility = View.VISIBLE
    }

    fun hideSecondaryActionButton() {
        secondaryActionButton.visibility = View.GONE
    }

    class PrimaryActionButtonClickedEvent
    class SecondaryActionButtonClickedEvent
}
