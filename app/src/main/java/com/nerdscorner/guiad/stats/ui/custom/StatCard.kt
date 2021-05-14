package com.nerdscorner.guiad.stats.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.extensions.toList

class StatCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val statLatestValue: TextView
    private val statLabel: TextView
    private val trendIcon: ImageView
    private val lineChart: LineChart

    init {
        LayoutInflater.from(context).inflate(R.layout.stat_card_item, this)
        setBackgroundResource(R.drawable.stat_card_background)

        val a = context.obtainStyledAttributes(attrs, R.styleable.StatCard, 0, 0)
        val statName = a.getString(R.styleable.StatCard_stat_Name)
        val statIcon = a.getResourceId(R.styleable.StatCard_stat_Icon, 0)
        a.recycle()

        findViewById<TextView>(R.id.stat_name).text = statName
        findViewById<ImageView>(R.id.stat_icon).setImageResource(statIcon)
        trendIcon = findViewById(R.id.stat_trend_icon)
        lineChart = findViewById(R.id.line_chart)
        statLatestValue = findViewById(R.id.stat_latest_value)
        statLabel = findViewById(R.id.stat_label)

        setupChartWidget()
    }

    private fun setupChartWidget() {
        lineChart.setNoDataText(context.getString(R.string.building_chart))
        lineChart.setNoDataTextColor(ContextCompat.getColor(context, R.color.graph_text_color))
        lineChart.xAxis.isEnabled = false
        lineChart.getAxis(YAxis.AxisDependency.LEFT).isEnabled = false
        lineChart.getAxis(YAxis.AxisDependency.RIGHT).isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.description = null
    }

    fun setup(chartData: ILineDataSet?, statLabel: String, latestValue: String, isTrendingUp: Boolean?, upIsBad: Boolean = true) {
        setup(chartData.toList(), statLabel, latestValue, isTrendingUp, upIsBad)
    }

    fun setup(chartData: List<ILineDataSet>?, statLabel: String, latestValue: String, isTrendingUp: Boolean?, upIsBad: Boolean = true) {
        if (chartData != null) {
            chartData.forEach {
                it.setDrawValues(false)
            }
            lineChart.data = LineData(chartData)
            lineChart.invalidate()
        }
        this.statLabel.text = statLabel
        statLatestValue.text = latestValue
        trendIcon.setImageResource(
            when {
                isTrendingUp == null -> R.drawable.ic_clock
                isTrendingUp -> {
                    if (upIsBad) {
                        R.drawable.ic_trend_up
                    } else {
                        R.drawable.ic_trend_down
                    }
                }
                else -> {
                    if (upIsBad) {
                        R.drawable.ic_trend_down
                    } else {
                        R.drawable.ic_trend_up
                    }
                }
            }
        )
        trendIcon.scaleY = if (isTrendingUp == false) {
            -1f
        } else {
            1f
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        findViewById<View>(R.id.view_more_button).setOnClickListener(listener)
    }
}
