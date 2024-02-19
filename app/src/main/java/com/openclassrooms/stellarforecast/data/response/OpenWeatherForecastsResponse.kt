package com.openclassrooms.stellarforecast.data.response

import com.openclassrooms.stellarforecast.domain.model.WeatherReportModel
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

    fun toDomainModel(): List<WeatherReportModel> {
        return forecasts.map { forecast ->
            val calendar = Calendar.getInstance().apply { timeInMillis = forecast.time * 1000L }


            // Check if the sky is clear (IDs 800 to 802 indicate clear sky conditions)
            val isClearSky = forecast.weather.isNotEmpty() && forecast.weather[0].id in 800..802


            // Get the hour of the date and determine if it's night
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val isNight = hourOfDay < 6 || hourOfDay >= 18


            // Convert temperature to Celsius
            val temperatureCelsius = (forecast.temperature.temp - 273.15).toInt()


            WeatherReportModel(
                isGoodForStargazing = isClearSky && isNight,
                date = calendar,
                temperatureCelsius = temperatureCelsius,
                weatherTitle = forecast.weather[0].title,
                weatherDescription = forecast.weather[0].description
            )
        }
    }
}
