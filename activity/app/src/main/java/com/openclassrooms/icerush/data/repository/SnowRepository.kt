package com.openclassrooms.icerush.data.repository

import android.util.Log
import com.openclassrooms.icerush.data.network.WeatherClient
import com.openclassrooms.icerush.domain.model.SnowReportModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import com.openclassrooms.icerush.data.repository.Result


class SnowRepository(private val dataService: WeatherClient) {
    private val API_KEY = "617aafb37c40e369ec5a9d14ca06b4c5"


    fun fetchForecastData(lat: Double, lng: Double): Flow<Result<List<SnowReportModel>>> =
        flow {
            emit(Result.Loading)
            val result = dataService.getWeatherByPosition(
                latitude = lat,
                longitude = lng,
                apiKey = API_KEY
            )
            val model = result.body()?.toDomainModel() ?: throw Exception("Invalid data")
            emit(Result.Success(model))
        }.catch { error ->
            emit(Result.Failure(error.message))
        }
}
