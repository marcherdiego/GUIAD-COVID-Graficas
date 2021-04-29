package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

abstract class DataObject(protected val csvLines: List<String>) {
    protected val dataLines = csvLines.drop(1) //Drop header
    
    abstract fun getStats(): List<Stat>
    
    fun getDataSet(
        dataLines: List<String>,
        dateIndex: Int,
        valueIndex: Int,
        label: String, 
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int
    ): ILineDataSet {
        val entries = dataLines.mapIndexed { index, line ->
            val dataToken = line.split(COMMA)
            Entry(index.toFloat(), dataToken[valueIndex].toFloat(), dataToken[dateIndex])
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
        const val COMMA = ","
    }
}
