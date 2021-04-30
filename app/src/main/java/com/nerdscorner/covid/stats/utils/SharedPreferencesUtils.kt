package com.nerdscorner.covid.stats.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtils {
    private lateinit var sharedPreferences: SharedPreferences

    private const val LAST_UPDATE_DATE_TIME = "last_update_date_time"

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    fun getLastUpdateDateTime(): String? {
        return sharedPreferences.getString(LAST_UPDATE_DATE_TIME, null)
    }

    fun setLastUpdateDateTime(lastUpdateDateTime: String) {
        sharedPreferences
            .edit()
            .putString(LAST_UPDATE_DATE_TIME, lastUpdateDateTime)
            .apply()
    }
}
