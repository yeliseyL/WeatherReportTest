package com.eliseylobanov.weatherreporttest.repository

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.eliseylobanov.weatherreporttest.Constants.API_KEY
import com.eliseylobanov.weatherreporttest.Constants.UNITS
import com.eliseylobanov.weatherreporttest.R
import com.eliseylobanov.weatherreporttest.database.CitiesDatabase
import com.eliseylobanov.weatherreporttest.database.DatabaseCity
import com.eliseylobanov.weatherreporttest.database.asDatabaseModel
import com.eliseylobanov.weatherreporttest.database.asDomainModel
import com.eliseylobanov.weatherreporttest.model.City
import com.eliseylobanov.weatherreporttest.network.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CitiesRepository(private val database: CitiesDatabase) {

    var cities: MutableLiveData<ArrayList<City>> =
            Transformations.map(database.citiesDao.getAllCities()) {
                it.asDomainModel()
            } as MutableLiveData<ArrayList<City>>

    suspend fun refreshCities() {
        withContext(Dispatchers.IO) {
            try {
                val cityList = mutableListOf<DatabaseCity>()
                cities.value?.let {
                    for (city in it) {
                        if (city.id != 0L) {
                            val result = WeatherApi.retrofitService.loadCity(city.name, UNITS, API_KEY)
                            cityList.add(result.asDatabaseModel())
                        }
                    }
                }
                database.citiesDao.insertAll(cityList)
            } catch (ex: Exception) {
                Log.e("CitiesRepository", Resources.getSystem().getString(R.string.no_connection))
            }
        }
    }

    suspend fun searchCity(name: String) {
        withContext(Dispatchers.IO) {
            try {
                val city = WeatherApi.retrofitService.loadCity(name, UNITS, API_KEY)
                database.citiesDao.insert(city.asDatabaseModel())
            } catch (ex: Exception) {
                Log.e("CitiesRepository", Resources.getSystem().getString(R.string.no_connection))
            }
        }
    }

    suspend fun searchCurrentCity(latitude: String?, longitude: String?) {
        withContext(Dispatchers.IO) {
            try {
                val city = WeatherApi.retrofitService.loadCity(latitude, longitude, UNITS, API_KEY)
                city.id = 0
                database.citiesDao.insert(city.asDatabaseModel())
            } catch (ex: Exception) {
                Log.e("CitiesRepository", Resources.getSystem().getString(R.string.no_connection))

            }
        }

    }
}