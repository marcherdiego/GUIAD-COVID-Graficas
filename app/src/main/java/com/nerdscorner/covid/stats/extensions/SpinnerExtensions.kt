package com.nerdscorner.covid.stats.extensions

import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import com.nerdscorner.covid.stats.domain.Stat
import com.nerdscorner.covid.stats.ui.adapter.StatsAdapter
import com.nerdscorner.covid.stats.ui.adapter.SimpleSpinnerAdapter

fun AppCompatSpinner.setItemSelectedListener(listener: (position: Int) -> Unit) {
    onItemSelectedListener = object : SimpleSpinnerAdapter() {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            listener(position)
        }
    }
}

fun AppCompatSpinner.setSelectedItemsChangedListener(listener: (selectedValues: List<Stat>) -> Unit) {
    (adapter as? StatsAdapter)?.setSelectedItemsChangedListener(listener)
}
