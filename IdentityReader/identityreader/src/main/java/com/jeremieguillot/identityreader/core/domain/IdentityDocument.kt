package com.jeremieguillot.identityreader.core.domain

import com.jeremieguillot.identityreader.core.extension.fromYYMMDDtoDate
import com.jeremieguillot.identityreader.core.extension.toLocaleDateStringSeparated
import java.util.Locale


data class IdentityDocument(
    val type: DocumentType,
    val documentNumber: String = "",
    val expirationDate: String = "",
    val deliveryDate: String = "",
    val origin: String = "",
    val size: Int = 0,

    val lastName: String = "",
    val firstName: String = "",
    val nationality: String = "",
    val birthDate: String = "",
    val placeOfBirth: String = "",
    val gender: String = "",
    val addressNumber: String = "",
    val address: String = "",
    val postalCode: String = "",
    val city: String = "",
    val country: String = "",
) {
    companion object {
        fun fromDataDocument(dataDocument: DataDocument): IdentityDocument {
            return IdentityDocument(
                type = DocumentType.ID_CARD, //TODO change
                documentNumber = dataDocument.documentNumber,
                origin = "",
                lastName = "",
                firstName = "",
                nationality = Locale("", dataDocument.issuingCountry).isO3Country,  // Missing data
                gender = dataDocument.sex,
                placeOfBirth = "",
                birthDate = dataDocument.dateOfBirth.fromYYMMDDtoDate()
                    ?.toLocaleDateStringSeparated() ?: "",
                expirationDate = dataDocument.dateOfExpiry.fromYYMMDDtoDate()
                    ?.toLocaleDateStringSeparated() ?: "",
                addressNumber = "",  // Missing data
                address = "",  // Missing data
                postalCode = "",  // Missing data
                city = "",
                country = ""  // Missing data
            )
        }
    }
}

fun Int.toHumanReadableHeight(): String {
    val meters = this / 100
    val centimeters = this % 100
    return "${meters}m${centimeters}"
}
