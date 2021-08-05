package com.eliseylobanov.weatherreporttest.network

import com.eliseylobanov.weatherreporttest.model.City
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun loadCity(@Query(value = "q", encoded = true) cityName: String?,
                         @Query("units") units: String?,
                         @Query("appid") keyApi: String?): City

    @GET("data/2.5/weather")
    suspend fun loadCity(@Query(value = "lat") latitude: String?,
                         @Query(value = "lon") longitude: String?,
                         @Query("units") units: String?,
                         @Query("appid") keyApi: String?): City

}