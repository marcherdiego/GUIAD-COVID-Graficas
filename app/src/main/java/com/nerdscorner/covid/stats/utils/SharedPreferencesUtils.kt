package com.nerdscorner.covid.stats.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtils {
    private lateinit var sharedPreferences: SharedPreferences

    private const val LAST_UPDATE_DATE_TIME = "last_update_date_time"
    private const val CITIES_DATA = "cities_data"
    private const val CTI_DATA = "cti_data"
    private const val DECEASES_DATA = "deceases_data"
    private const val GENERAL_DATA = "general_data"
    private const val P7_BY_CITY_DATA = "p7_by_city_data"
    private const val P7_DATA = "p7_data"

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

    private fun saveString(key: String, value: String?) {
        sharedPreferences
            .edit()
            .putString(key, value)
            .apply()
    }
}
