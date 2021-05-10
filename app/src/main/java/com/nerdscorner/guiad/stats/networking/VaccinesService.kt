package com.nerdscorner.guiad.stats.networking

import retrofit2.http.GET

interface VaccinesService {
    @GET("Uruguay.csv")
    suspend fun getCountryData(): String

    @GET("Subnational.csv")
    suspend fun getDataByCity(): String

    @GET("Age.csv")
    suspend fun getDataByAge(): String

    @GET("Segments.csv")
    suspend fun getDataBySegment(): String
}
