package com.nerdscorner.covid.stats.ui.mvp.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
    private val chart: LineChart = activity.findViewById(R.id.chart)
    private val legendsContainer: FlowLayout = activity.findViewById(R.id.legends_container)

    init {
        chart.setNoDataText("No hay datos seleccionados...")
        chart.isHighlightPerDragEnabled = true
        chart.isHighlightPerTapEnabled = true
        chart.marker = ChartMarker(activity, R.layout.custom_chart_marker)
        chart.legend.isEnabled = false
        
        datePicker.setDatePickedListener {
            bus.post(DatePickedEvent(it))
        }
        onClick(R.id.back_day_button, BackDayButtonClickedEvent())
        onClick(R.id.back_month_button, BackMonthButtonClickedEvent())
        onClick(R.id.forward_day_button, ForwardDayButtonClickedEvent())
        onClick(R.id.forward_month_button, ForwardMonthButtonClickedEvent())

        listOf(
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
        ).forEach { rawStat ->
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
                    return try {
                        dataSet.getEntryForIndex(value.toInt()).data.toString()
                    } catch (e: Exception) {
                        super.getAxisLabel(value, axis)
                    }
                }
            }
        }
    }

    class DatePickedEvent(val date: Date)

    class StatClickedEvent(val rawStat: RawStat)

    class BackDayButtonClickedEvent
    class BackMonthButtonClickedEvent
    class ForwardDayButtonClickedEvent
    class ForwardMonthButtonClickedEvent
}
