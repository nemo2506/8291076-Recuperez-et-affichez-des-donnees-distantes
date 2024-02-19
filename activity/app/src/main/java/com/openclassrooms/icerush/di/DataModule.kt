package com.openclassrooms.icerush.di

import com.openclassrooms.icerush.data.network.WeatherClient
import com.openclassrooms.icerush.data.repository.SnowRepository
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
    fun provideWeatherRepository(dataClient: WeatherClient): SnowRepository {
        return SnowRepository(dataClient)
    }
}
