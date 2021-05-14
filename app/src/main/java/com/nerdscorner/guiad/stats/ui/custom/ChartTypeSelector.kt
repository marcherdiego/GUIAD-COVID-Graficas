package com.nerdscorner.guiad.stats.ui.custom

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.domain.ChartType
import com.nerdscorner.guiad.stats.extensions.animateScaleUp


class ChartTypeSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val unselectedColorFilter = ColorMatrixColorFilter(
        ColorMatrix().apply {
            setSaturation(0f)
        }
    )
    private val selectedColorFilter = ColorMatrixColorFilter(
        ColorMatrix().apply {
            setSaturation(1f)
        }
    )

    private var listener: ((ChartType) -> Unit)? = null
    private var selectedChartType: ImageView? = null
    private var linesChartButton: ImageView
    private var barChartButton: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.chart_type_selector_layout, this)

        linesChartButton = findViewById(R.id.lines_chart_button)
        barChartButton = findViewById(R.id.bar_chart_button)

        selectedChartType = linesChartButton
        linesChartButton.colorFilter = selectedColorFilter
        barChartButton.colorFilter = unselectedColorFilter

        linesChartButton.setOnClickListener {
            selectChartType(linesChartButton)
            listener?.invoke(ChartType.LINE)
            it.animateScaleUp()
        }
        barChartButton.setOnClickListener {
            selectChartType(barChartButton)
            listener?.invoke(ChartType.BAR)
            it.animateScaleUp()
        }
    }

    private fun selectChartType(chartTypeButton: ImageView) {
        selectedChartType?.colorFilter = unselectedColorFilter
        chartTypeButton.colorFilter = selectedColorFilter
        selectedChartType = chartTypeButton
    }

    fun setOnChartTypeSelectedListener(listener: (ChartType) -> Unit) {
        this.listener = listener
    }

    fun setSelectedChartType(chartType: ChartType) {
        when(chartType) {
            ChartType.LINE -> linesChartButton.performClick()
            ChartType.BAR -> barChartButton.performClick()
        }
    }
}
