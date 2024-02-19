package com.openclassrooms.icerush.data.repository

import com.openclassrooms.icerush.data.network.WeatherClient
import com.openclassrooms.icerush.data.response.OpenWeatherForecastsResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherRepositoryTest {

    private lateinit var cut: SnowRepository //Class Under Test
    private lateinit var dataService: WeatherClient

    @Before
    fun setup() {
        dataService = mockk()
        cut = SnowRepository(dataService)
    }


    @Test
    fun `assert when fetchForecastData is requested then clean data is provided`() = runTest {
        //given
        val openForecastResponse = OpenWeatherForecastsResponse(
            listOf(
                OpenWeatherForecastsResponse.ForecastResponse(
                    1,
                    OpenWeatherForecastsResponse.ForecastResponse.TemperatureResponse(130.0),
                    listOf(
                        OpenWeatherForecastsResponse.ForecastResponse.WeatherResponse(
                            800,
                            "title",
                            "description"
                        )
                    )
                )
            )
        )

        coEvery {
            dataService.getWeatherByPosition(any(), any(), any())
        } returns Response.success(openForecastResponse)

        //when
        val values = run {
            cut.fetchForecastData(0.0, 0.0).toList()
        }

        //then
        coVerify { dataService.getWeatherByPosition(any(), any(), any()) }
        assertEquals(2, values.size)
        assertEquals(Result.Loading, values[0])
        assertEquals(Result.Success(openForecastResponse.toDomainModel()), values[1])
    }


    @Test
    fun `assert when fetchForecastData fail then result failure is raise`() = runTest {
        //given
        val errorResponseBody = "Error message".toResponseBody("text/plain".toMediaType())
        val response = Response.error<OpenWeatherForecastsResponse>(404, errorResponseBody)
        coEvery {
            dataService.getWeatherByPosition(
                any(),
                any(),
                any()
            )
        } returns response

        //when
        val values = run {
            cut.fetchForecastData(0.0, 0.0).toList()
        }

        //then
        coVerify { dataService.getWeatherByPosition(any(), any(), any()) }
        assertEquals(2, values.size)
        assertEquals(values[0], Result.Loading)
        assert(values[1] is Result.Failure)
    }
}