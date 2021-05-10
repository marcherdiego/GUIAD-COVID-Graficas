package com.nerdscorner.guiad.stats.networking

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ServiceGenerator {
    private const val GUIAD_BASE_URL = "https://raw.githubusercontent.com/GUIAD-COVID/datos-y-visualizaciones-GUIAD/master/datos/"
    private const val VACCINES_BASE_URL = "https://raw.githubusercontent.com/3dgiordano/covid-19-uy-vacc-data/main/data/"

    private var guiadRetrofit = getRetrofit(GUIAD_BASE_URL)
    private var vaccinesRetrofit = getRetrofit(VACCINES_BASE_URL)

    private fun getRetrofit(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(
            OkHttpClient
                .Builder()
                .build()
        )
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()

    fun <T> createGuiadService(clazz: Class<T>): T = guiadRetrofit.create(clazz)

    fun <T> createVaccinesService(clazz: Class<T>): T = vaccinesRetrofit.create(clazz)
}
