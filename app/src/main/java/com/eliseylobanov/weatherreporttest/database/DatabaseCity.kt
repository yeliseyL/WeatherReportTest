package com.eliseylobanov.weatherreporttest.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eliseylobanov.weatherreporttest.model.City
import com.eliseylobanov.weatherreporttest.model.Main
import com.eliseylobanov.weatherreporttest.model.Weather
import com.eliseylobanov.weatherreporttest.model.Wind

@Entity(tableName = "city_table")
class DatabaseCity(
        @PrimaryKey
        val id: Long,
        val name: String,
        val temp: Double,
        val description: String,
        val speed: Double
)

fun List<DatabaseCity>.asDomainModel(): ArrayList<City> {
    return map {
        City(
                id = it.id,
                main = Main(it.temp),
                name = it.name,
                weather = listOf(Weather(description = it.description)),
                wind = Wind(it.speed)
        )
    } as ArrayList<City>
}

fun City.asDatabaseModel(): DatabaseCity {
    return DatabaseCity(
            id = this.id,
            name = this.name,
            temp = this.main.temp,
            description = this.weather[0].description,
            speed = this.wind.speed
    )
}