package com.nerdscorner.guiad.stats.ui.mvp.view

import android.view.LayoutInflater
import android.view.View
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.domain.ChartType
import com.nerdscorner.guiad.stats.domain.Stat
import com.nerdscorner.guiad.stats.extensions.setItemSelectedListener
import com.nerdscorner.guiad.stats.extensions.setSelectedItemsChangedListener
import com.nerdscorner.guiad.stats.ui.adapter.StatsAdapter
import com.nerdscorner.guiad.stats.ui.custom.ChartMarker
import com.nerdscorner.guiad.stats.ui.custom.ChartTypeSelector
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nex3z.flowlayout.FlowLayout

abstract class StatsView(activity: AppCompatActivity) : BaseActivityView(activity) {
    private val legendsContainer: FlowLayout = activity.findViewById(R.id.legends_container)
    private val citiesSelector: AppCompatSpinner? = activity.findViewById(R.id.cities_selector)
    private val statSelector: AppCompatSpinner = activity.findViewById(R.id.stat_selector)
    private val rangeSelector: AppCompatSpinner = activity.findViewById(R.id.data_limit_selector)
    private val chartTypeSelector: ChartTypeSelector = activity.findViewById(R.id.chart_type_selector)

    private val lineChart: LineChart = activity.findViewById(R.id.line_chart)
    private val barChart: BarChart = activity.findViewById(R.id.bar_chart)

    init {
        activity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        citiesSelector?.setItemSelectedListener {
            bus.post(CitySelectedEvent(it))
        }
        rangeSelector.setItemSelectedListener {
            bus.post(RangeSelectedEvent(it))
        }
        chartTypeSelector.setOnChartTypeSelectedListener {
            bus.post(ChartTypeSelectedEvent(it))
        }

        styleChartView(lineChart)
        styleChartView(barChart)
        barChart.visibility = View.INVISIBLE
    }

    private fun styleChartView(chart: BarLineChartBase<*>) {
        val activity = activity ?: return
        val chartPadding = activity.resources.getDimensionPixelSize(R.dimen.chart_padding).toFloat()
        chart.setNoDataText(activity.getString(R.string.no_data_selected))
        chart.setNoDataTextColor(ContextCompat.getColor(activity, R.color.graph_text_color))
        chart.setExtraOffsets(chartPadding, chartPadding, chartPadding, chartPadding)
        chart.isHighlightPerTapEnabled = true
        chart.isHighlightPerDragEnabled = true
        chart.marker = ChartMarker(activity, R.layout.custom_chart_marker)
        chart.legend.isEnabled = false
        chart.description = null
    }

    fun setSelectedChartType(chartType: ChartType) {
        chartTypeSelector.setSelectedChartType(chartType)
    }

    fun setCitiesAdapter(adapter: SpinnerAdapter) {
        citiesSelector?.adapter = adapter
    }

    fun setStatsAdapter(adapter: StatsAdapter) {
        statSelector.adapter = adapter
        statSelector.setSelectedItemsChangedListener {
            adapter.notifyDataSetChanged()
            bus.post(StatsSelectedEvent(it))
        }
    }

    fun setRangesAdapter(adapter: SpinnerAdapter) {
        rangeSelector.adapter = adapter
    }

    fun setSelectedCity(index: Int) {
        citiesSelector?.setSelection(index)
    }

    fun setSelectedRange(index: Int) {
        rangeSelector.setSelection(index)
    }

    fun showLineChart() {
        lineChart.visibility = View.VISIBLE
    }

    fun hideLineChart() {
        lineChart.visibility = View.INVISIBLE
    }

    fun showBarChart() {
        barChart.visibility = View.VISIBLE
    }

    fun hideBarChart() {
        barChart.visibility = View.INVISIBLE
    }

    fun setLineChartData(dataSets: List<ILineDataSet>) {
        lineChart.clear()
        legendsContainer.removeAllViews()
        if (dataSets.isNotEmpty()) {
            styleAxis(lineChart, dataSets.first())
            lineChart.data = LineData(dataSets)
            lineChart.legend.entries.forEach { legend ->
                val legendView = LayoutInflater.from(activity).inflate(R.layout.chart_legend_item, null).apply {
                    findViewById<View>(R.id.indicator).setBackgroundColor(legend.formColor)
                    findViewById<TextView>(R.id.legend).text = legend.label
                }
                legendsContainer.addView(legendView)
            }
            lineChart.invalidate()
        }
    }

    fun setBarChartData(dataSets: List<IBarDataSet>) {
        barChart.clear()
        legendsContainer.removeAllViews()
        if (dataSets.isNotEmpty()) {
            styleAxis(barChart, dataSets.first())
            val widthSpaceRatio = 4
            val groupSpace = 0.06f
            val barSpace = (1f - groupSpace) / (dataSets.size.toFloat() * (widthSpaceRatio + 1))
            val barWidth = widthSpaceRatio * barSpace
            barChart.data = BarData(dataSets).apply {
                this.barWidth = barWidth
            }
            if (dataSets.size > 1) {
                barChart.groupBars(0f, groupSpace, barSpace)
            }
            barChart.legend.entries.forEach { legend ->
                val legendView = LayoutInflater.from(activity).inflate(R.layout.chart_legend_item, null).apply {
                    findViewById<View>(R.id.indicator).setBackgroundColor(legend.formColor)
                    findViewById<TextView>(R.id.legend).text = legend.label
                }
                legendsContainer.addView(legendView)
            }
            barChart.invalidate()
        }
    }

    private fun styleAxis(chart: BarLineChartBase<*>, dataSet: IDataSet<*>) {
        val chartTextColor = ContextCompat.getColor(activity ?: return, R.color.graph_text_color)
        chart.getAxis(YAxis.AxisDependency.LEFT).textColor = chartTextColor
        chart.getAxis(YAxis.AxisDependency.RIGHT).textColor = chartTextColor
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTH_SIDED
            textColor = chartTextColor
            granularity = 1f
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return try {
                        val entry = dataSet.getEntryForIndex(value.toInt())
                        entry.data.toString()
                    } catch (_: Exception) {
                        ""
                    }
                }
            }
        }
    }

    class CitySelectedEvent(val position: Int)
    class StatsSelectedEvent(val selectedStats: ArrayList<Stat>)
    class RangeSelectedEvent(val position: Int)
    class ChartTypeSelectedEvent(val chartType: ChartType)
}
