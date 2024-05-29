package com.jeremieguillot.identityreader.domain

data class MRZ(
    val documentNumber: String,
    val dateOfBirth: String,
    val dateOfExpiry: String
)
