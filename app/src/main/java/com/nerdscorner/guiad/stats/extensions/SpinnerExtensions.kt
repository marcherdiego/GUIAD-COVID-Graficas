package com.nerdscorner.guiad.stats.extensions

import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import com.nerdscorner.guiad.stats.ui.adapter.SimpleSpinnerAdapter

fun AppCompatSpinner.setItemSelectedListener(listener: (position: Int) -> Unit) {
    onItemSelectedListener = object : SimpleSpinnerAdapter() {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            listener(position)
        }
    }
}
