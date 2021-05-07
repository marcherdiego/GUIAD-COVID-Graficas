package com.nerdscorner.guiad.stats.extensions

import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import com.nerdscorner.guiad.stats.domain.Stat
import com.nerdscorner.guiad.stats.ui.adapter.StatsAdapter
import com.nerdscorner.guiad.stats.ui.adapter.SimpleSpinnerAdapter

fun AppCompatSpinner.setItemSelectedListener(listener: (position: Int) -> Unit) {
    onItemSelectedListener = object : SimpleSpinnerAdapter() {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            listener(position)
        }
    }
}

fun AppCompatSpinner.setSelectedItemsChangedListener(listener: (selectedValues: ArrayList<Stat>) -> Unit) {
    (adapter as? StatsAdapter)?.setSelectedItemsChangedListener(listener)
}
