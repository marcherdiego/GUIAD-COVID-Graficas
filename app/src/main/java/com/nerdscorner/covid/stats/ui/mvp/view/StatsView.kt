package com.nerdscorner.covid.stats.ui.mvp.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.R
import com.nerdscorner.mvplib.events.view.BaseActivityView

abstract class StatsView(activity: AppCompatActivity) : BaseActivityView(activity) {
    private val chart: LineChart = activity.findViewById(R.id.chart)

    fun setChartsData(dataSets: List<ILineDataSet>) {
        chart.data = LineData(dataSets)
        styleAxis(dataSets.first())
        chart.invalidate()
    }

    fun setChartsData(dataSet: ILineDataSet) {
        chart.data = LineData(dataSet)
        styleAxis(dataSet)
        chart.invalidate()
    }
    
    private fun styleAxis(dataSet: ILineDataSet) {
        chart.getAxis(YAxis.AxisDependency.RIGHT).textColor = Color.WHITE
        chart.getAxis(YAxis.AxisDependency.LEFT).textColor = Color.WHITE
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTH_SIDED
            textColor = Color.WHITE
            granularity = 1f
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    val entry = dataSet.getEntryForIndex(value.toInt())
                    return entry.data.toString()
                }
            }
        }
    }
}
