package com.example.realty.interfaces

import com.example.realty.models.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    fun getWeather(@Query("q") cityName : String,
    @Query("appid") apiKey : String) : Call<Weather>;
}