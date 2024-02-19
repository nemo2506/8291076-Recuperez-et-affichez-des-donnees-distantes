package com.openclassrooms.stellarforecast.data.network

import com.openclassrooms.stellarforecast.data.response.OpenWeatherForecastsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherClient {
    @GET("/data/2.5/forecast")
    suspend fun getWeatherByPosition(
        @Query(value = "lat") latitude: Double,
        @Query(value = "lon") longitude: Double,
        @Query(value = "appid") apiKey: String
    ): Response<OpenWeatherForecastsResponse>
}
