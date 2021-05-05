package com.nerdscorner.covid.stats.ui.mvp.view

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.domain.Stat
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.covid.stats.ui.activities.RawDataGeneralStatsActivity
import com.nerdscorner.covid.stats.ui.custom.ChartMarker
import com.nerdscorner.covid.stats.ui.custom.DatePicker
import com.nerdscorner.covid.stats.ui.custom.RawStat
import com.nex3z.flowlayout.FlowLayout
import java.util.*

class RawDataGeneralStatsView(activity: RawDataGeneralStatsActivity) : BaseActivityView(activity) {
    private val datePicker: DatePicker = activity.findViewById(R.id.date_picker)

    private val backDayButton: ImageView = activity.findViewById(R.id.back_day_button)
    private val backMonthButton: ImageView = activity.findViewById(R.id.back_month_button)
    private val forwardDayButton: ImageView = activity.findViewById(R.id.forward_day_button)
    private val forwardMonthButton: ImageView = activity.findViewById(R.id.forward_month_button)

    private val newCases: RawStat = activity.findViewById(R.id.new_cases)
    private val totalCases: RawStat = activity.findViewById(R.id.total_cases)
    private val ctiCases: RawStat = activity.findViewById(R.id.cti_cases)
    private val activeCases: RawStat = activity.findViewById(R.id.active_cases)
    private val recoveredCases: RawStat = activity.findViewById(R.id.recovered_cases)
    private val totalRecovered: RawStat = activity.findViewById(R.id.total_Recovered)
    private val deceases: RawStat = activity.findViewById(R.id.deceases)
    private val totalDeceases: RawStat = activity.findViewById(R.id.total_deceases)
    private val newTests: RawStat = activity.findViewById(R.id.new_tests)
    private val positivity: RawStat = activity.findViewById(R.id.positivity)
    private val harvardIndex: RawStat = activity.findViewById(R.id.harvard_index)
    private val indexVariation: RawStat = activity.findViewById(R.id.index_variation)
    private val rawStatsList = listOf(
        newCases,
        totalCases,
        ctiCases,
        activeCases,
        recoveredCases,
        totalRecovered,
        deceases,
        totalDeceases,
        newTests,
        positivity,
        harvardIndex
    )

    private val chart: LineChart = activity.findViewById(R.id.chart)
    private val legendsContainer: FlowLayout = activity.findViewById(R.id.legends_container)

    var manualHighlightUpdate = false

    init {
        activity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        chart.setNoDataText(activity.getString(R.string.no_data_selected))
        chart.setNoDataTextColor(ContextCompat.getColor(activity, R.color.graph_text_color))
        chart.isHighlightPerDragEnabled = true
        chart.isHighlightPerTapEnabled = true
        chart.marker = ChartMarker(activity, R.layout.custom_chart_marker)
        chart.legend.isEnabled = false
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry?, h: Highlight?) {
                if (manualHighlightUpdate) {
                    return
                }
                bus.post(ChartValueSelectedEvent(entry))
            }

            override fun onNothingSelected() {
            }
        })

        datePicker.setDatePickedListener {
            bus.post(DatePickedEvent(it))
        }
        onClick(backDayButton, BackDayButtonClickedEvent())
        onClick(backMonthButton, BackMonthButtonClickedEvent())
        onClick(forwardDayButton, ForwardDayButtonClickedEvent())
        onClick(forwardMonthButton, ForwardMonthButtonClickedEvent())
        onClick(R.id.today_button, TodayButtonClickedEvent())

        rawStatsList.forEach { rawStat ->
            rawStat.setOnClickListener {
                bus.post(StatClickedEvent(rawStat))
            }
        }
    }

    fun setMinDate(date: Date?) {
        datePicker.setMinDate(date)
    }

    fun setDate(date: Date) {
        datePicker.setDate(date, manualUpdate = true)
    }

    fun getStats(
        newCases: Stat,
        totalCases: Stat,
        ctiCases: Stat,
        activeCases: Stat,
        recoveredCases: Stat,
        totalRecovered: Stat,
        deceases: Stat,
        totalDeceases: Stat,
        newTests: Stat,
        positivity: Stat,
        harvardIndex: Stat
    ) {
        this.newCases.stat = newCases
        this.totalCases.stat = totalCases
        this.ctiCases.stat = ctiCases
        this.activeCases.stat = activeCases
        this.recoveredCases.stat = recoveredCases
        this.totalRecovered.stat = totalRecovered
        this.deceases.stat = deceases
        this.totalDeceases.stat = totalDeceases
        this.newTests.stat = newTests
        this.positivity.stat = positivity
        this.harvardIndex.stat = harvardIndex
    }

    fun getStatsValues(
        newCases: String,
        totalCases: String,
        ctiCases: String,
        activeCases: String,
        recoveredCases: String,
        totalRecovered: String,
        deceases: String,
        totalDeceases: String,
        newTests: String,
        positivity: String,
        harvardIndex: String,
        indexVariation: String
    ) {
        this.newCases.setValue(newCases)
        this.totalCases.setValue(totalCases)
        this.ctiCases.setValue(ctiCases)
        this.activeCases.setValue(activeCases)
        this.recoveredCases.setValue(recoveredCases)
        this.totalRecovered.setValue(totalRecovered)
        this.deceases.setValue(deceases)
        this.totalDeceases.setValue(totalDeceases)
        this.newTests.setValue(newTests)
        this.positivity.setValue(positivity)
        this.harvardIndex.setValue(harvardIndex)
        this.indexVariation.setValue(indexVariation)
    }

    fun setChartsData(dataSets: List<ILineDataSet>) {
        chart.clear()
        legendsContainer.removeAllViews()
        styleAxis(dataSets.firstOrNull() ?: return)
        chart.data = LineData(dataSets)
        chart.invalidate()
        chart.legend.entries.forEach { legend ->
            val legendView = LayoutInflater.from(activity).inflate(R.layout.chart_legend_item, null).apply {
                findViewById<View>(R.id.indicator).setBackgroundColor(legend.formColor)
                findViewById<TextView>(R.id.legend).text = legend.label
            }
            legendsContainer.addView(legendView)
        }
    }

    fun setChartSelectedItem(x: Float) {
        if (x == -1f) {
            chart.highlightValue(null)
        } else {
            chart.data?.dataSets?.let {
                chart.highlightValue(x, 0)
            }
        }
    }

    private fun styleAxis(dataSet: ILineDataSet) {
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
                        dataSet.getEntryForIndex(value.toInt()).data.toString()
                    } catch (e: Exception) {
                        super.getAxisLabel(value, axis)
                    }
                }
            }
        }
    }

    fun disableForwardButtons() {
        val disabledColorFilter = ContextCompat.getColor(activity ?: return, R.color.raw_stat_buttons_unselected_color)
        forwardDayButton.setColorFilter(disabledColorFilter, PorterDuff.Mode.SRC_IN)
        forwardMonthButton.setColorFilter(disabledColorFilter, PorterDuff.Mode.SRC_IN)
    }

    fun enableForwardButtons() {
        val enabledColorFilter = ContextCompat.getColor(activity ?: return, R.color.raw_stat_buttons_selected_color)
        forwardDayButton.setColorFilter(enabledColorFilter, PorterDuff.Mode.SRC_IN)
        forwardMonthButton.setColorFilter(enabledColorFilter, PorterDuff.Mode.SRC_IN)
    }

    fun disableBackButtons() {
        val disabledColorFilter = ContextCompat.getColor(activity ?: return, R.color.raw_stat_buttons_unselected_color)
        backDayButton.setColorFilter(disabledColorFilter, PorterDuff.Mode.SRC_IN)
        backMonthButton.setColorFilter(disabledColorFilter, PorterDuff.Mode.SRC_IN)
    }

    fun enableBackButtons() {
        val enabledColorFilter = ContextCompat.getColor(activity ?: return, R.color.raw_stat_buttons_selected_color)
        backDayButton.setColorFilter(enabledColorFilter, PorterDuff.Mode.SRC_IN)
        backMonthButton.setColorFilter(enabledColorFilter, PorterDuff.Mode.SRC_IN)
    }

    fun refreshSelectedRawStats(selectedStats: ArrayList<Stat>) {
        rawStatsList.forEach { rawStat ->
            rawStat.itemSelected = rawStat.stat in selectedStats
            rawStat.updateSelectedState()
        }
    }

    class DatePickedEvent(val date: Date)
    class ChartValueSelectedEvent(val entry: Entry?)
    class StatClickedEvent(val rawStat: RawStat)

    class BackDayButtonClickedEvent
    class BackMonthButtonClickedEvent
    class ForwardDayButtonClickedEvent
    class ForwardMonthButtonClickedEvent
    class TodayButtonClickedEvent
}
