package com.nerdscorner.covid.stats.networking

import retrofit2.http.GET

interface StatsService {
    @GET("estadisticasUY.csv")
    suspend fun getGeneralStats(): String

    @GET("estadisticasUY_cti.csv")
    suspend fun getCtiStats(): String

    @GET("estadisticasUY_porDepto_detalle.csv")
    suspend fun getStatsByCity(): String

    @GET("estadisticasUY_fallecimientos.csv")
    suspend fun getDeceases(): String

    @GET("estadisticasUY_p7.csv")
    suspend fun getP7StatisticsByCity(): String

    @GET("estadisticasUY_p7nacional.csv")
    suspend fun getP7Statistics(): String

    @GET("movilidad_uy.csv")
    suspend fun getMobilityStats(): String
}
