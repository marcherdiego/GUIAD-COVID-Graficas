package com.nerdscorner.guiad.stats.domain

import androidx.annotation.ColorInt
import androidx.annotation.WorkerThread
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.extensions.formatNumberString
import com.nerdscorner.guiad.stats.extensions.roundToString
import com.nerdscorner.guiad.stats.utils.RangeUtils
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils
import kotlin.math.min

abstract class DataObject {
    protected var dataLines = mutableListOf<MutableList<String>>()

    private val standardValueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float) = value.roundToString()
    }

    abstract fun getStats(): List<Stat>

    protected abstract fun persist(data: String?)

    @WorkerThread
    open fun setData(data: String?) {
        dataLines = data
            ?.split(LINE_FEED)
            ?.drop(1)
            ?.map {
                it.split(COMMA).toMutableList()
            }
            ?.toMutableList()
            ?: mutableListOf() //Drop header
        persist(data)
    }

    open fun getLatestValue(stat: Stat): String {
        val latestValue = getValueAt(dataLines.size - 1, stat)
            ?.toFloatOrNull()
            ?.times(stat.factor)
            ?: return N_A
        return latestValue.roundToString().formatNumberString()
    }

    open fun isTrendingUp(stat: Stat): Boolean {
        if (dataLines.size < 2) {
            return false
        }
        val latestValue = getValueAt(dataLines.size - 1, stat)
            ?.toFloatOrNull()
            ?.times(stat.factor)
            ?: 0f
        val preLatestValue = getValueAt(dataLines.size - 2, stat)
            ?.toFloatOrNull()
            ?.times(stat.factor)
            ?: 0f
        return (latestValue - preLatestValue) > 0
    }

    protected fun getLineTokensAt(row: Int) = dataLines.getOrNull(row)

    protected fun getValueAt(row: Int, stat: Stat) = getLineTokensAt(row)?.get(stat.index)

    protected fun getLineDataSet(
        dataLines: List<List<String>>,
        dateIndex: Int,
        valueIndex: Int,
        statFactorMultiplier: Float,
        label: String,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int?
    ): ILineDataSet {
        val entries = dataLines
            .takeLast(getDataLimit(dataLines, limit))
            .mapIndexed { index, line ->
                Entry(index.toFloat(), (line[valueIndex].toFloatOrNull() ?: 0f) * statFactorMultiplier, line[dateIndex])
            }
        return LineDataSet(entries, label).apply {
            this.color = color
            this.valueTextColor = valueTextColor
            this.circleColors = colors
            this.highLightColor = color
            this.isHighlightEnabled = true
            this.setDrawHighlightIndicators(true)
            this.valueFormatter = standardValueFormatter
        }
    }

    protected fun getBarDataSet(
        dataLines: List<List<String>>,
        dateIndex: Int,
        valueIndex: Int,
        statFactorMultiplier: Float,
        label: String,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int?
    ): IBarDataSet {
        val entries = dataLines
            .takeLast(getDataLimit(dataLines, limit))
            .mapIndexed { index, line ->
                BarEntry(index.toFloat(), (line[valueIndex].toFloatOrNull() ?: 0f) * statFactorMultiplier, line[dateIndex])
            }
        return BarDataSet(entries, label).apply {
            this.color = color
            this.valueTextColor = valueTextColor
            this.highLightColor = color
            this.isHighlightEnabled = true
            this.valueFormatter = standardValueFormatter
        }
    }

    protected fun getSelectedDataRange(): Int {
        val selectedRangeIndex = SharedPreferencesUtils.getSelectedDataRangeIndex()
        return RangeUtils.getDaysCountForRangeIndex(selectedRangeIndex, dataLines.size)
    }

    private fun getDataLimit(dataLines: List<List<String>>, limit: Int?): Int {
        return limit ?: min(dataLines.size, getSelectedDataRange())
    }

    companion object {
        const val EMPTY_STRING = ""
        const val LINE_FEED = "\n"
        private const val COMMA = ","
        const val N_A = "N/A"
    }
}
