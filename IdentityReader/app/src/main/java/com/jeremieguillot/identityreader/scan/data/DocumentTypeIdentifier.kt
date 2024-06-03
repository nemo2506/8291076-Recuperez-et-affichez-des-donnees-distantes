package com.jeremieguillot.identityreader.scan.data

import com.jeremieguillot.identityreader.scan.domain.IdentifierContent
import com.jeremieguillot.identityreader.scan.domain.IdentifierType
import java.util.regex.Pattern

class DocumentTypeIdentifier {

    private val REGEX_OLD_PASSPORT =
        "(?<documentNumber>[A-Z0-9<]{9})(?<checkDigitDocumentNumber>[0-9ILDSOG]{1})(?<nationality>[A-Z<]{3})(?<dateOfBirth>[0-9ILDSOG]{6})(?<checkDigitDateOfBirth>[0-9ILDSOG]{1})(?<sex>[FM<]){1}(?<expirationDate>[0-9ILDSOG]{6})(?<checkDigitExpiration>[0-9ILDSOG]{1})"
    private val REGEX_IP_PASSPORT_LINE_1 = "\\bIP[A-Z<]{3}[A-Z0-9<]{9}[0-9]{1}"
    private val REGEX_IP_PASSPORT_LINE_2 = "[0-9]{6}[0-9]{1}[FM<]{1}[0-9]{6}[0-9]{1}[A-Z<]{3}"

    fun identify(mrz: String): IdentifierContent {

        val matcher = Pattern.compile(REGEX_OLD_PASSPORT).matcher(mrz)
        val oldPassportMatcher = matcher.find()

        return when {
            oldPassportMatcher -> IdentifierContent(IdentifierType.OLD_PASSPORT, matcher)
            else -> IdentifierContent(IdentifierType.UNKNOWN, null)
        }
    }
}