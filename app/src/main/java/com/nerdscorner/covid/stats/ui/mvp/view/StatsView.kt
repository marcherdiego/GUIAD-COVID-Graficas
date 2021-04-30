package com.nerdscorner.covid.stats.ui.mvp.view

import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.domain.Stat
import com.nerdscorner.covid.stats.extensions.setItemSelectedListener
import com.nerdscorner.covid.stats.extensions.setSelectedItemsChangedListener
import com.nerdscorner.covid.stats.ui.adapter.StatsAdapter
import com.nerdscorner.covid.stats.ui.custom.ChartMarker
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nex3z.flowlayout.FlowLayout

abstract class StatsView(activity: AppCompatActivity) : BaseActivityView(activity) {
    private val isPortrait = activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    private val chart: LineChart = activity.findViewById(R.id.chart)
    private val legendsContainer: FlowLayout = activity.findViewById(R.id.legends_container)
    private val citiesSelector: AppCompatSpinner? = activity.findViewById(R.id.cities_selector)
    protected val statSelector: AppCompatSpinner = activity.findViewById(R.id.stat_selector)

    init {
        citiesSelector?.setItemSelectedListener {
            bus.post(CitySelectedEvent(it))
        }
        chart.setNoDataText("No hay datos seleccionados...")
        chart.isHighlightPerDragEnabled = true
        chart.isHighlightPerTapEnabled = true
        chart.marker = ChartMarker(activity, R.layout.custom_chart_marker)
        chart.legend.apply {
            orientation = if (isPortrait) {
                Legend.LegendOrientation.HORIZONTAL
            } else {
                Legend.LegendOrientation.VERTICAL
            }
            textColor = Color.WHITE
            isWordWrapEnabled = true
            xEntrySpace = activity.resources.getDimensionPixelSize(R.dimen.legend_horizontal_margin).toFloat()

            isEnabled = false
        }
    }

    fun setCitiesAdapter(adapter: SpinnerAdapter) {
        citiesSelector?.adapter = adapter
    }

    fun setStatsAdapter(adapter: StatsAdapter) {
        statSelector.adapter = adapter
        statSelector.setSelectedItemsChangedListener {
            bus.post(StatsSelectedEvent(it))
        }
    }

    fun setChartsData(dataSets: List<ILineDataSet>) {
        chart.clear()
        styleAxis(dataSets.firstOrNull() ?: return)
        chart.data = LineData(dataSets)
        chart.invalidate()
        legendsContainer.removeAllViews()
        chart.legend.entries.forEach { legend ->
            val legendView = LayoutInflater.from(activity).inflate(R.layout.chart_legend_item, null).apply {
                findViewById<View>(R.id.indicator).setBackgroundColor(legend.formColor)
                findViewById<TextView>(R.id.legend).text = legend.label
            }
            legendsContainer.addView(legendView)
        }
    }

    private fun styleAxis(dataSet: ILineDataSet) {
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

    class CitySelectedEvent(val position: Int)
    class StatsSelectedEvent(val selectedStats: List<Stat>)
}
