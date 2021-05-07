package com.nerdscorner.guiad.stats.ui.mvp.view

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.mvplib.events.view.BaseFragmentView

class ProgressDialogView(dialog: DialogFragment) : BaseFragmentView(dialog) {
    private val progressbar: ProgressBar
    private val tickImageView: ImageView
    private val dialogText: TextView

    init {
        with (dialog.requireView()) {
            progressbar = findViewById(R.id.ui_progressbar)
            tickImageView = findViewById(R.id.ui_imageview)
            dialogText = findViewById(R.id.text)
        }
    }

    fun setMessage(message: String?) {
        dialogText.text = message
    }

    fun showProgressBar() {
        progressbar.visibility = View.VISIBLE
        tickImageView.visibility = View.GONE
    }

    fun showTickImage() {
        progressbar.visibility = View.GONE
        tickImageView.visibility = View.VISIBLE
    }

    fun hideMessage() {
        dialogText.visibility = View.GONE
    }

    fun showMessage() {
        dialogText.visibility = View.VISIBLE
    }
}
