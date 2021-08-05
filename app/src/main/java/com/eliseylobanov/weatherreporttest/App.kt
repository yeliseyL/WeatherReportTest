package com.eliseylobanov.weatherreporttest

import android.app.Application
import com.eliseylobanov.weatherreporttest.di.application
import com.eliseylobanov.weatherreporttest.di.detailsScreen
import com.eliseylobanov.weatherreporttest.di.mainscreen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherReportApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(listOf(application, mainscreen, detailsScreen))
        }
    }
}