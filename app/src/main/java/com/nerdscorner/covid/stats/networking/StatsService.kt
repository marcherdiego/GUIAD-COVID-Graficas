package com.nerdscorner.covid.stats.networking

import retrofit2.Call
import retrofit2.http.GET

interface StatsService {
    @GET("estadisticasUY.csv")
    fun getGeneralStats(): Call<String>

    @GET("estadisticasUY_cti.csv")
    fun getCtiStats(): Call<String>

    @GET("estadisticasUY_porDepto_detalle.csv")
    fun getStatsByCity(): Call<String>

    @GET("estadisticasUY_fallecimientos.csv")
    fun getDeceases(): Call<String>
}
