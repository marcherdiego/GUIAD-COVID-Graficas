package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

abstract class DataObject {
    protected lateinit var csvLines: List<String>
    protected lateinit var dataLines: List<String>

    fun setData(data: String?) {
        csvLines = data?.split(LINE_FEED) ?: listOf()
        dataLines = csvLines.drop(1) //Drop header
    }

    abstract fun getStats(): List<Stat>

    protected fun getDataSet(
        dataLines: List<String>,
        dateIndex: Int,
        valueIndex: Int,
        statFactorMultiplier: Float,
        label: String,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int
    ): ILineDataSet {
        val entries = dataLines
            .filterNot {
                it.trim() == EMPTY_STRING
            }
            .mapIndexed { index, line ->
                val dataToken = line.split(COMMA)
                Entry(index.toFloat(), (dataToken[valueIndex].toFloatOrNull() ?: 0f) * statFactorMultiplier, dataToken[dateIndex])
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

    companion object {
        const val EMPTY_STRING = ""
        const val LINE_FEED = "\n"
        const val COMMA = ","
    }
}
