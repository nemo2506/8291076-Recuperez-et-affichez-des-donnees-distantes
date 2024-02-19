package com.openclassrooms.stellarforecast.di

import com.openclassrooms.stellarforecast.data.network.WeatherClient
import com.openclassrooms.stellarforecast.data.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideWeatherRepository(dataClient: WeatherClient): WeatherRepository {
        return WeatherRepository(dataClient)
    }
}
