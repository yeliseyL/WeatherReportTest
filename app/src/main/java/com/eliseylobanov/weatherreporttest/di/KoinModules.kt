package com.eliseylobanov.weatherreporttest.di

import androidx.room.Room
import com.eliseylobanov.weatherreporttest.database.CitiesDatabase
import com.eliseylobanov.weatherreporttest.model.City
import com.eliseylobanov.weatherreporttest.repository.CitiesRepository
import com.eliseylobanov.weatherreporttest.ui.details.DetailsViewModel
import com.eliseylobanov.weatherreporttest.ui.main.MainViewModel
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), CitiesDatabase::class.java, "cities").build() }
    single { get<CitiesDatabase>().citiesDao }
    single { CitiesRepository(get()) }
}

val mainscreen = module { factory { MainViewModel(get()) } }
val detailsScreen = module { factory { (city: City) -> DetailsViewModel(city) } }