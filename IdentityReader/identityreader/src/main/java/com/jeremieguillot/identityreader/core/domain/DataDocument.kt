package com.jeremieguillot.identityreader.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class DataDocument(
    val issuingCountry: String,
    val documentNumber: String,
    val lastName: String,
    val dateOfBirth: String,
    val dateOfExpiry: String,
    val sex: String
) : Parcelable {
    companion object {
        val EMPTY = DataDocument("", "", "", "", "", "")
    }
}