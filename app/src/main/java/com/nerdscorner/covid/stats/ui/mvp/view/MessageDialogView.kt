package com.nerdscorner.covid.stats.ui.mvp.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.nerdscorner.covid.stats.R
import com.nerdscorner.mvplib.events.view.BaseFragmentView

class MessageDialogView(dialog: DialogFragment) : BaseFragmentView(dialog) {
    private val tickImageView: ImageView
    private val dialogText: TextView

    init {
        with (dialog.requireView()) {
            tickImageView = findViewById(R.id.ui_imageview)
            dialogText = findViewById(R.id.text)
            onClick(R.id.action_button, ActionButtonClickedEvent())
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
    
    class ActionButtonClickedEvent
}
