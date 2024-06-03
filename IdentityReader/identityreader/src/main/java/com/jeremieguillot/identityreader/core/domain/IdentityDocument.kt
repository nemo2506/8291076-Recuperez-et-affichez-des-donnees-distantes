package com.jeremieguillot.identityreader.core.domain

data class IdentityDocument(
    val type: DocumentType,
    val documentNumber: String,
//    val expirationDate: LocalDate,
    val origin: String,

    val lastName: String,
    val firstName: String,
    val nationality: String,
//    val birthDate: LocalDate,
    val gender: String,
    val addressNumber: String,
    val address: String,
    val postalCode: String,
    val city: String,
    val country: String,
)


