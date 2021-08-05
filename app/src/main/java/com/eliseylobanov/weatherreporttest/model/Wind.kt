package com.eliseylobanov.weatherreporttest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wind(
        val speed: Double
) : Parcelable