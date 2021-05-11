package com.nerdscorner.guiad.stats.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nerdscorner.guiad.stats.domain.*
import com.nerdscorner.guiad.stats.utils.ColorUtils
import com.nerdscorner.guiad.stats.utils.SharedPreferencesUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                // Init shared preferences
                SharedPreferencesUtils.init(this@SplashActivity)

                // Init chart colors array
                ColorUtils.init(this@SplashActivity)

                // Load pre-saved data onto data objects
                CitiesData.getInstance().setData(SharedPreferencesUtils.getCitiesData())
                CtiData.getInstance().setData(SharedPreferencesUtils.getCtiData())
                DeceasesData.getInstance().setData(SharedPreferencesUtils.getDeceasesData())
                GeneralStatsData.getInstance().setData(SharedPreferencesUtils.getGeneralData())
                P7ByCityData.getInstance().setData(SharedPreferencesUtils.getP7ByCityData())
                P7Data.getInstance().setData(SharedPreferencesUtils.getP7Data())
                MobilityData.getInstance().setData(SharedPreferencesUtils.getMobilityData())

                VaccinesBySegmentData.getInstance().setData(SharedPreferencesUtils.getVaccinesBySegmentData())
                VaccinesByAgeData.getInstance().setData(SharedPreferencesUtils.getVaccinesByAgeData())
                VaccinesData.getInstance().setData(SharedPreferencesUtils.getVaccinesGlobalData())
                VaccinesByCityData.getInstance().setData(SharedPreferencesUtils.getVaccinesByCityData())
            }

            // After finishing initializing, start MainActivity
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}