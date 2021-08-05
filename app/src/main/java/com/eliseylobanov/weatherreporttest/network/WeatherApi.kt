package com.eliseylobanov.weatherreporttest.network

import com.eliseylobanov.weatherreporttest.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApi {
    private val retrofitWeather = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

    val retrofitService: WeatherApiService = retrofitWeather.create(WeatherApiService::class.java)
}