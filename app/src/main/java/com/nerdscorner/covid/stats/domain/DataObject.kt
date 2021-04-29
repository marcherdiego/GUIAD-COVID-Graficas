package com.nerdscorner.covid.stats.domain

import androidx.annotation.ColorInt
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

abstract class DataObject(protected val csvLines: List<String>) {
    protected val dataLines = csvLines.drop(1) //Drop header
    
    abstract val dataOffset: Int
    abstract fun getStats(): List<Stat>
    
    fun getDataSet(
        dataLines: List<String>,
        dateIndex: Int,
        dataIndex: Int,
        @ColorInt color: Int,
        @ColorInt valueTextColor: Int
    ): ILineDataSet {
        val headers = getStats().map { it.name }
        val entries = dataLines.mapIndexed { index, line ->
            val dataToken = line.split(COMMA)
            Entry(index.toFloat(), dataToken[dataIndex].toFloat(), dataToken[dateIndex])
        }
        return LineDataSet(entries, headers[dataIndex - dataOffset]).apply {
            this.color = color
            this.valueTextColor = valueTextColor
            this.circleColors = colors
        }
    }
    
    companion object {
        const val COMMA = ","
    }
}
