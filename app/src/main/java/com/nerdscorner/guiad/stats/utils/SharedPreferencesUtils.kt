package com.nerdscorner.guiad.stats.utils

import android.content.Context
import android.content.SharedPreferences
import com.nerdscorner.guiad.stats.domain.ChartType

object SharedPreferencesUtils {
    private lateinit var sharedPreferences: SharedPreferences

    private const val LAST_UPDATE_DATE_TIME = "last_update_date_time"
    private const val CITIES_DATA = "cities_data"
    private const val CTI_DATA = "cti_data"
    private const val DECEASES_DATA = "deceases_data"
    private const val GENERAL_DATA = "general_data"
    private const val P7_BY_CITY_DATA = "p7_by_city_data"
    private const val P7_DATA = "p7_data"
    private const val MOBILITY_DATA = "mobility_data"

    private const val VACCINES_BY_SEGMENT_DATA = "vaccines_by_segment_data"
    private const val VACCINES_BY_AGE_DATA = "vaccines_by_age_data"
    private const val VACCINES_GLOBAL_DATA = "vaccines_global_data"
    private const val VACCINES_BY_CITY_DATA = "vaccines_by_city_data"

    private const val ROTATE_DEVICE_SHOWN = "rotate_device_shown"
    private const val SELECTED_DATA_RANGE_INDEX = "data_range_index"
    private const val SELECTED_CHART_TYPE_INDEX = "chart_type_index"

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    fun getLastUpdateDateTime() = sharedPreferences.getString(LAST_UPDATE_DATE_TIME, null)

    fun saveLastUpdateDateTime(lastUpdateDateTime: String) {
        saveString(LAST_UPDATE_DATE_TIME, lastUpdateDateTime)
    }

    fun saveCitiesData(data: String?) {
        saveString(CITIES_DATA, data)
    }

    fun getCitiesData() = sharedPreferences.getString(CITIES_DATA, null)

    fun saveCtiData(data: String?) {
        saveString(CTI_DATA, data)
    }

    fun getCtiData() = sharedPreferences.getString(CTI_DATA, null)

    fun saveDeceasesData(data: String?) {
        saveString(DECEASES_DATA, data)
    }

    fun getDeceasesData() = sharedPreferences.getString(DECEASES_DATA, null)

    fun saveGeneralData(data: String?) {
        saveString(GENERAL_DATA, data)
    }

    fun getGeneralData() = sharedPreferences.getString(GENERAL_DATA, null)

    fun saveP7ByCityData(data: String?) {
        saveString(P7_BY_CITY_DATA, data)
    }

    fun getP7ByCityData() = sharedPreferences.getString(P7_BY_CITY_DATA, null)

    fun saveP7Data(data: String?) {
        saveString(P7_DATA, data)
    }

    fun getP7Data() = sharedPreferences.getString(P7_DATA, null)

    fun saveMobilityData(data: String?) {
        saveString(MOBILITY_DATA, data)
    }

    fun getMobilityData() = sharedPreferences.getString(MOBILITY_DATA, null)

    fun saveVaccinesBySegmentData(data: String?) {
        saveString(VACCINES_BY_SEGMENT_DATA, data)
    }

    fun getVaccinesBySegmentData() = sharedPreferences.getString(VACCINES_BY_SEGMENT_DATA, null)

    fun saveVaccinesByAgeData(data: String?) {
        saveString(VACCINES_BY_AGE_DATA, data)
    }

    fun getVaccinesByAgeData() = sharedPreferences.getString(VACCINES_BY_AGE_DATA, null)

    fun saveVaccinesGlobalData(data: String?) {
        saveString(VACCINES_GLOBAL_DATA, data)
    }

    fun getVaccinesGlobalData() = sharedPreferences.getString(VACCINES_GLOBAL_DATA, null)

    fun saveVaccinesByCityData(data: String?) {
        saveString(VACCINES_BY_CITY_DATA, data)
    }

    fun getVaccinesByCityData() = sharedPreferences.getString(VACCINES_BY_CITY_DATA, null)

    fun setRotateDeviceDialogShown(shown: Boolean) {
        saveBoolean(ROTATE_DEVICE_SHOWN, shown)
    }

    fun getRotateDeviceDialogShown() = sharedPreferences.getBoolean(ROTATE_DEVICE_SHOWN, false)

    fun getSelectedDataRangeIndex() = sharedPreferences.getInt(SELECTED_DATA_RANGE_INDEX, 0)

    fun saveSelectedDataRangeIndex(rangeIndex: Int) {
        saveInt(SELECTED_DATA_RANGE_INDEX, rangeIndex)
    }

    fun getSelectedChartType(): ChartType {
        val index = sharedPreferences.getInt(SELECTED_CHART_TYPE_INDEX, 0)
        return ChartType.values()[index]
    }

    fun saveSelectedChartType(chartType: ChartType) {
        val chartTypeIndex = ChartType.values().indexOf(chartType)
        saveInt(SELECTED_CHART_TYPE_INDEX, chartTypeIndex)
    }

    private fun saveString(key: String, value: String?) {
        sharedPreferences
            .edit()
            .putString(key, value)
            .apply()
    }

    private fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(key, value)
            .apply()
    }

    private fun saveInt(key: String, value: Int) {
        sharedPreferences
            .edit()
            .putInt(key, value)
            .apply()
    }
}
