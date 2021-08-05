package com.eliseylobanov.weatherreporttest.ui.main

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliseylobanov.weatherreporttest.R
import com.eliseylobanov.weatherreporttest.WeatherApiStatus
import com.eliseylobanov.weatherreporttest.model.City
import com.eliseylobanov.weatherreporttest.repository.CitiesRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: CitiesRepository) : ViewModel() {

    var cities = repository.cities

    private val _navigateToSelectedCity = MutableLiveData<City?>()
    val navigateToSelectedCity: LiveData<City?>
        get() = _navigateToSelectedCity

    private val _status = MutableLiveData<WeatherApiStatus>()
    val status: LiveData<WeatherApiStatus>
        get() = _status

    init {
        getCities()
    }

    private fun getCities() {
        viewModelScope.launch {
            try {
                _status.value = WeatherApiStatus.LOADING
                repository.refreshCities()
                cities = repository.cities
                _status.value = WeatherApiStatus.DONE
            } catch (ex: Exception) {
                Log.e("Repository", Resources.getSystem().getString(R.string.no_network))
                _status.value = WeatherApiStatus.ERROR
            }
        }
    }

    fun searchCity(name: String) {
        viewModelScope.launch {
            try {
                _status.value = WeatherApiStatus.LOADING
                repository.searchCity(name)
                _status.value = WeatherApiStatus.DONE
            } catch (ex: Exception) {
                _status.value = WeatherApiStatus.ERROR
                Log.e("Repository", "Wrong city name")
            }
        }
    }

    fun searchCurrentCity(latitude: String?, longitude: String?) {
        viewModelScope.launch {
            try {
                _status.value = WeatherApiStatus.LOADING
                repository.searchCurrentCity(latitude, longitude)
                _status.value = WeatherApiStatus.DONE
            } catch (ex: Exception) {
                Log.e("Repository", Resources.getSystem().getString(R.string.no_network))
                _status.value = WeatherApiStatus.ERROR
            }
        }
    }

    fun displayCityDetails(city: City) {
        _navigateToSelectedCity.value = city
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedCity.value = null
    }
}