package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.extensions.roundToString
import com.nerdscorner.covid.stats.utils.RangeUtils
import com.nerdscorner.covid.stats.utils.SharedPreferencesUtils
import kotlin.math.min

abstract class DataObject {
    protected lateinit var dataLines: MutableList<String>

    open fun setData(data: String?) {
        dataLines = data
            ?.split(LINE_FEED)
            ?.drop(1)
            ?.toMutableList() 
            ?: mutableListOf() //Drop header
        persist(data)
    }

    abstract fun getStats(): List<Stat>

    open fun getLatestValue(stat: Stat): String {
        val latestValue = getValueAt(dataLines.size - 1, stat)
            ?.toFloatOrNull()
            ?.times(stat.factor) 
            ?: return N_A
        return latestValue.roundToString()
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

    protected abstract fun persist(data: String?)

    protected fun getLineTokensAt(row: Int) = dataLines.getOrNull(row)?.split(COMMA)

    protected fun getValueAt(row: Int, stat: Stat) = getLineTokensAt(row)?.get(stat.index)

    protected fun getDataSet(
        dataLines: List<String>,
        dateIndex: Int,
        valueIndex: Int,
        statFactorMultiplier: Float,
        label: String,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int,
        limit: Int?
    ): ILineDataSet {
        val entries = dataLines
            .filterNot { it.trim() == EMPTY_STRING }
            .takeLast(getDataLimit(dataLines, limit))
            .mapIndexed { index, line ->
                val dataTokens = line.split(COMMA)
                Entry(index.toFloat(), (dataTokens[valueIndex].toFloatOrNull() ?: 0f) * statFactorMultiplier, dataTokens[dateIndex])
            }
        return LineDataSet(entries, label).apply {
            this.color = color
            this.valueTextColor = valueTextColor
            this.circleColors = colors
            this.highLightColor = color
            this.isHighlightEnabled = true
            this.setDrawHighlightIndicators(true)
        }
    }

    private fun getDataLimit(dataLines: List<String>, limit: Int?): Int {
        return if (limit != null) {
            limit
        } else {
            val selectedRangeIndex = SharedPreferencesUtils.getSelectedDataRangeIndex()
            val selectedDataRange = RangeUtils.getDaysCountForRangeIndex(selectedRangeIndex, dataLines.size)
            min(dataLines.size, selectedDataRange)
        }
    }

    companion object {
        const val EMPTY_STRING = ""
        const val LINE_FEED = "\n"
        const val COMMA = ","
        const val N_A = "N/A"
    }
}
