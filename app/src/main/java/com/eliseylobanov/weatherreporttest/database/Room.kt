package com.eliseylobanov.weatherreporttest.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CitiesDao {

    @Query("SELECT * FROM city_table")
    fun getAllCities(): LiveData<List<DatabaseCity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<DatabaseCity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: DatabaseCity)
}

@Database(entities = [DatabaseCity::class], version = 1)
abstract class CitiesDatabase : RoomDatabase() {
    abstract val citiesDao: CitiesDao
}

