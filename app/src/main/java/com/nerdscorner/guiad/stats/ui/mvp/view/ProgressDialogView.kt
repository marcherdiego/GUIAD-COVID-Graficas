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
    private val dialogText: TextView

    init {
        with (dialog.requireView()) {
            progressbar = findViewById(R.id.ui_progressbar)
            dialogText = findViewById(R.id.text)
        }
    }

    fun setMessage(message: String?) {
        dialogText.text = message
    }

    fun hideMessage() {
        dialogText.visibility = View.GONE
    }

    fun showMessage() {
        dialogText.visibility = View.VISIBLE
    }
}
