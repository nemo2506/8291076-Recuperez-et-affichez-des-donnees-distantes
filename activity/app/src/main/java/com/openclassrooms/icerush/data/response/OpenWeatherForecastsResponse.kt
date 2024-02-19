package com.openclassrooms.icerush.data.response

import com.openclassrooms.icerush.domain.model.SnowReportModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Calendar

@JsonClass(generateAdapter = true)
data class OpenWeatherForecastsResponse(
    @Json(name = "list")
    val forecasts: List<ForecastResponse>,
) {


    @JsonClass(generateAdapter = true)
    data class ForecastResponse(
        @Json(name = "dt")
        val time: Int,
        @Json(name = "main")
        val temperature: TemperatureResponse,
        @Json(name = "weather")
        val weather: List<WeatherResponse>,
    ) {

        @JsonClass(generateAdapter = true)
        data class TemperatureResponse(
            @Json(name = "temp")
            val temp: Double,
        )

        @JsonClass(generateAdapter = true)
        data class WeatherResponse(
            @Json(name = "id")
            val id: Int,
            @Json(name = "main")
            val title: String,
            @Json(name = "description")
            val description: String
        )
    }
    fun toDomainModel(): List<SnowReportModel> {
        return forecasts.map { forecast ->
            val calendar = Calendar.getInstance().apply { timeInMillis = forecast.time * 1000L }

            // Convert temperature to Celsius
            val temperatureCelsius = (forecast.temperature.temp - 273.15).toInt()

            val isRaining = forecast.weather[0].id in 500..531

            SnowReportModel(
                isRaining = isRaining,
                date = calendar,
                temperatureCelsius = temperatureCelsius,
                weatherTitle = forecast.weather[0].title,
                weatherDescription = forecast.weather[0].description
            )
        }
    }

}
