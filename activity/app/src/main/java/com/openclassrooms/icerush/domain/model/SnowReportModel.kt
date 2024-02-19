package com.openclassrooms.icerush.domain.model

import java.util.Calendar

data class SnowReportModel(
    val date: Calendar,
    val isRaining: Boolean,
    val temperatureCelsius: Int,
    val weatherTitle: String,
    val weatherDescription: String
)
