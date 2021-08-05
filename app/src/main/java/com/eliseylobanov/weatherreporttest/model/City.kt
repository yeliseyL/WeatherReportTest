package com.eliseylobanov.weatherreporttest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
        var id: Long,
        val main: Main,
        val name: String,
        val weather: List<Weather>,
        val wind: Wind
) : Parcelable

