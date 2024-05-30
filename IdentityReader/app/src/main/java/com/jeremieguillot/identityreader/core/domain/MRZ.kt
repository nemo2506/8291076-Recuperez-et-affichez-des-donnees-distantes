package com.jeremieguillot.identityreader.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MRZ(
    val documentNumber: String,
    val dateOfBirth: String,
    val dateOfExpiry: String
) : Parcelable