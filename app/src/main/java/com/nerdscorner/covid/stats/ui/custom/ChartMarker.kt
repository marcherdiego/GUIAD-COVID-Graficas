package com.nerdscorner.covid.stats.ui.custom

import android.content.Context
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.nerdscorner.covid.stats.R

class ChartMarker(context: Context, @LayoutRes layoutResource: Int) : MarkerView(context, layoutResource) {
    private val markerTextView: TextView = findViewById(R.id.marker_label)
    private var mOffset: MPPointF? = null

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        markerTextView.text = "${e?.data} - ${e?.y?.toString()}"
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        if (mOffset == null) {
            mOffset = MPPointF(-(5 * width / 4).toFloat(), -(5 * height / 4).toFloat())
        }
        return mOffset!!
    }
}
