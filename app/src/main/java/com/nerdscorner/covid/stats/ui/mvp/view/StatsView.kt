package com.nerdscorner.covid.stats.ui.mvp.view

import android.app.Activity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.activities.StatsActivity
import com.nerdscorner.mvplib.events.view.BaseActivityView

class StatsView(activity: StatsActivity) : BaseActivityView(activity) {
    private val chart: LineChart = activity.findViewById(R.id.chart)

    init {
        //buildCtiGraphs(getAssetsFileLines("data/estadisticasUY_cti.csv"))
        buildCitiesGraphs(getAssetsFileLines("data/estadisticasUY_porDepto_detalle.csv"))
    }

    private fun getAssetsFileLines(fileName: String): List<String>? {
        return activity
            ?.assets
            ?.open(fileName)
            ?.bufferedReader()
            ?.use {
                it.readText()
            }
            ?.split("\n")
    }

    private fun buildCtiGraphs(csvLines: List<String>?) {
        val activity = activity ?: return
        val dataSets = Cti.getDataSets(csvLines ?: return, activity)
        chart.data = LineData(dataSets)
        chart.invalidate()
    }

    private fun buildCitiesGraphs(csvLines: List<String>?) {
        val activity = activity ?: return
        val dataSets = City.getDataSets(csvLines ?: return, activity)
        chart.data = LineData(dataSets)
        chart.invalidate()
    }

    companion object {

        private fun getDataSet(
            activity: Activity,
            headers: List<String>,
            dataLines: List<String>,
            dateIndex: Int,
            dataIndex: Int,
            color: Int,
            valueTextColor: Int
        ): ILineDataSet {
            val entries = arrayListOf<Entry>()
            dataLines.forEachIndexed { lineIndex, line ->
                val dataToken = line.split(",")
                entries.add(Entry(lineIndex.toFloat(), dataToken[dataIndex].toFloat(), dataToken[dateIndex]))
            }
            return LineDataSet(entries, headers[dataIndex]).apply {
                this.color = ContextCompat.getColor(activity, color)
                this.valueTextColor = ContextCompat.getColor(activity, valueTextColor)
            }
        }

        object Cti {
            private const val INDEX_DATE = 1
            private const val INDEX_PATIENTS_QUANTITY = 2
            private const val INDEX_TOTAL_OCCUPATION = 3
            private const val INDEX_COVID_OCCUPATION = 4
            private const val INDEX_OPERATIVE_BEDS = 5
            private const val INDEX_OCCUPIED_BEDS = 6
            private const val INDEX_NEW_PATIENTS = 7
            private const val INDEX_DECEASES = 8
            private const val INDEX_MEDICAL_DISCHARGES = 9

            fun getDataSets(csvLines: List<String>, activity: Activity): List<ILineDataSet> {
                val headers = csvLines.first().split(",")
                val dataSets = mutableListOf<ILineDataSet>()
                val dataLines = csvLines.drop(1) //Drop header

                dataSets.add(getDataSet(activity, headers, dataLines, INDEX_DATE, INDEX_PATIENTS_QUANTITY, R.color.graph1, R.color.graph1))
                dataSets.add(getDataSet(activity, headers, dataLines, INDEX_DATE, INDEX_TOTAL_OCCUPATION, R.color.graph2, R.color.graph2))
                dataSets.add(getDataSet(activity, headers, dataLines, INDEX_DATE, INDEX_COVID_OCCUPATION, R.color.graph3, R.color.graph3))
                dataSets.add(getDataSet(activity, headers, dataLines, INDEX_DATE, INDEX_OPERATIVE_BEDS, R.color.graph4, R.color.graph4))
                dataSets.add(getDataSet(activity, headers, dataLines, INDEX_DATE, INDEX_OCCUPIED_BEDS, R.color.graph5, R.color.graph5))
                dataSets.add(getDataSet(activity, headers, dataLines, INDEX_DATE, INDEX_NEW_PATIENTS, R.color.graph6, R.color.graph6))
                dataSets.add(getDataSet(activity, headers, dataLines, INDEX_DATE, INDEX_DECEASES, R.color.graph7, R.color.graph7))
                dataSets.add(getDataSet(activity, headers, dataLines, INDEX_DATE, INDEX_MEDICAL_DISCHARGES, R.color.graph8, R.color.graph8))
                return dataSets
            }
        }

        object City {
            private const val INDEX_DATE = 0
            private const val INDEX_CITY = 1
            private const val INDEX_IN_COURSE = 2
            private const val INDEX_NEW_CASES_CALCULATED = 3
            private const val INDEX_NEW_CASES_APP = 4
            private const val INDEX_NEW_DECEASES = 5
            private const val INDEX_TOTAL_DECEASES = 6
            private const val INDEX_NEW_RECOVERED = 7
            private const val INDEX_TOTAL_RECOVERED = 8
            private const val INDEX_TOTAL_CASES = 8

            private const val MONTEVIDEO = "Montevideo(UY-MO)"

            fun getDataSets(csvLines: List<String>, activity: Activity): List<ILineDataSet> {
                val headers = csvLines.first().split(",")
                val dataSets = mutableListOf<ILineDataSet>()

                val dataLines = csvLines
                    .asSequence()
                    .drop(1)
                    .groupBy {
                        it.split(",")[City.INDEX_DATE]
                    }
                    .map { dateEntries ->
                        dateEntries
                            .value
                            .map {
                                it.split(",")[City.INDEX_TOTAL_CASES].toInt()
                            }
                            .reduce { acc, s -> acc + s }
                            .toString()
                    }
                dataSets.add(getDataSet(activity, headers, dataLines, 0, 0, R.color.graph1, R.color.graph1))
                return dataSets
            }
        }
    }
}
