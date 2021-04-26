package com.nerdscorner.covid.stats.ui.mvp.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.R
import com.nerdscorner.mvplib.events.view.BaseActivityView

abstract class StatsView(activity: AppCompatActivity) : BaseActivityView(activity) {
    private val chart: LineChart = activity.findViewById(R.id.chart)

    fun setChartsData(dataSets: List<ILineDataSet>) {
        chart.data = LineData(dataSets)
        chart.getAxis(YAxis.AxisDependency.RIGHT).textColor = Color.WHITE
        chart.invalidate()
    }
}
