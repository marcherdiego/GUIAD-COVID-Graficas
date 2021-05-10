package com.nerdscorner.guiad.stats.ui.mvp.view

import android.widget.TextView
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.guiad.stats.ui.activities.MainActivity
import com.nerdscorner.guiad.stats.ui.custom.StatCard

class MainView(activity: MainActivity) : BaseActivityView(activity) {
    private val ctiCard: StatCard = activity.findViewById(R.id.cti_card)
    private val citiesCard: StatCard = activity.findViewById(R.id.cities_card)
    private val generalsCard: StatCard = activity.findViewById(R.id.general_stats_card)
    private val deceasesCard: StatCard = activity.findViewById(R.id.deceases_stats_card)
    private val p7Card: StatCard = activity.findViewById(R.id.p7_stats_card)
    private val mobilityCard: StatCard = activity.findViewById(R.id.mobility_stats_card)
    private val rawDataCard: StatCard = activity.findViewById(R.id.raw_data_general_stats_card)
    private val vaccinesBySegmentCard: StatCard = activity.findViewById(R.id.vaccines_general_stats_card)

    private val lastUpdated: TextView = activity.findViewById(R.id.last_updated)

    fun setupCtiCard(chartData: ILineDataSet?, statLabel: String, latestValue: String, isTrendingUp: Boolean?) {
        ctiCard.setup(chartData, statLabel, latestValue, isTrendingUp)
        ctiCard.setOnClickListener {
            bus.post(CtiButtonClickedEvent())
        }
    }

    fun setupCitiesCard(chartData: List<ILineDataSet>?, statLabel: String, latestValue: String, isTrendingUp: Boolean?) {
        citiesCard.setup(chartData, statLabel, latestValue, isTrendingUp)
        citiesCard.setOnClickListener {
            bus.post(CitiesButtonClickedEvent())
        }
    }

    fun setupGeneralsCard(chartData: ILineDataSet?, statLabel: String, latestValue: String, isTrendingUp: Boolean?) {
        generalsCard.setup(chartData, statLabel, latestValue, isTrendingUp)
        generalsCard.setOnClickListener {
            bus.post(GeneralStatsButtonClickedEvent())
        }
    }

    fun setupDeceasesCard(chartData: ILineDataSet?, statLabel: String, latestValue: String, isTrendingUp: Boolean?) {
        deceasesCard.setup(chartData, statLabel, latestValue, isTrendingUp)
        deceasesCard.setOnClickListener {
            bus.post(DeceasesStatsButtonClickedEvent())
        }
    }

    fun setupP7Card(chartData: ILineDataSet?, statLabel: String, latestValue: String, isTrendingUp: Boolean?) {
        p7Card.setup(chartData, statLabel, latestValue, isTrendingUp)
        p7Card.setOnClickListener {
            bus.post(P7StatsButtonClickedEvent())
        }
    }

    fun setupMobilityCard(chartData: ILineDataSet?, statLabel: String, latestValue: String, isTrendingUp: Boolean?) {
        mobilityCard.setup(chartData, statLabel, latestValue, isTrendingUp)
        mobilityCard.setOnClickListener {
            bus.post(MobilityStatsButtonClickedEvent())
        }
    }

    fun setupRawDataCard(chartData: ILineDataSet?, statLabel: String, latestValue: String, isTrendingUp: Boolean?) {
        rawDataCard.setup(chartData, statLabel, latestValue, isTrendingUp)
        rawDataCard.setOnClickListener {
            bus.post(RawDataGeneralStatsButtonClickedEvent())
        }
    }

    fun setupVaccinesBySegmentCard(chartData: ILineDataSet?, statLabel: String, latestValue: String, isTrendingUp: Boolean?) {
        vaccinesBySegmentCard.setup(chartData, statLabel, latestValue, isTrendingUp)
        vaccinesBySegmentCard.setOnClickListener {
            bus.post(VaccinesBySegmentStatsButtonClickedEvent())
        }
    }

    fun setLastUpdateDate(lastUpdateText: String) {
        lastUpdated.text = lastUpdateText
    }

    // Charts
    class CtiButtonClickedEvent
    class CitiesButtonClickedEvent
    class GeneralStatsButtonClickedEvent
    class DeceasesStatsButtonClickedEvent
    class P7StatsButtonClickedEvent
    class MobilityStatsButtonClickedEvent
    class VaccinesBySegmentStatsButtonClickedEvent

    // Raw data
    class RawDataGeneralStatsButtonClickedEvent
}
