package com.jeremieguillot.identityreader.core.presentation

import com.jeremieguillot.identityreader.core.domain.MRZ
import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {

    @Serializable
    data object ScannerScreen : Destination()

    @Serializable
    data class ReaderScreen(val mrz: MRZ) : Destination()
}