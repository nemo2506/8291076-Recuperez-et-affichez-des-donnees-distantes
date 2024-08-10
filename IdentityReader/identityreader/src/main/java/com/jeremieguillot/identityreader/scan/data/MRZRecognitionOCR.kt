package com.jeremieguillot.identityreader.scan.data

import com.google.mlkit.vision.text.Text
import com.jeremieguillot.identityreader.core.domain.DataDocument
import com.jeremieguillot.identityreader.core.domain.DocumentType
import com.jeremieguillot.identityreader.core.domain.util.DataError
import com.jeremieguillot.identityreader.core.domain.util.Result
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * The optical zone of a biometric passport, also known as the Machine Readable Zone (MRZ), consists of two lines of text containing essential information about the passport holder and the document itself. This information is encoded to be quickly readable by machines. Here's the composition of each line, section by section:
 *
 * ### First line of the Machine Readable Zone (MRZ)
 * 1. **Positions 1-2**: Document type
 *    - Typically "P" for passport.
 * 2. **Positions 3-5**: Issuing country code
 *    - A three-letter code corresponding to ISO 3166-1 alpha-3.
 * 3. **Positions 6-44**: Full name
 *    - The name is separated by two chevrons ("<<") between the surname and given names. Spaces in names are also represented by chevrons.
 *
 * ### Second line of the Machine Readable Zone (MRZ)
 * 1. **Positions 1-9**: Passport number
 *    - Followed by a check digit at position 10.
 * 2. **Positions 11-13**: Nationality code
 *    - A three-letter code corresponding to ISO 3166-1 alpha-3 of the holder's nationality.
 * 3. **Positions 14-19**: Date of birth
 *    - YYMMDD format, followed by a check digit at position 20.
 * 4. **Position 21**: Gender
 *    - "M" for male, "F" for female, or "X" for unspecified.
 * 5. **Positions 22-27**: Passport expiration date
 *    - YYMMDD format, followed by a check digit at position 28.
 * 6. **Positions 29-42**: Personal identification number (if applicable)
 *    - Sometimes used for a national identity card number or other relevant identifier, followed by a check digit at position 43.
 * 7. **Positions 44-44**: Overall check character
 *    - This check character is calculated over all the data from both lines to verify the integrity of the information.
 *
 * ### Example MRZ for a passport:
 * First line:
 * ```
 * P<UTOERIKSSON<<ANNA<MARIA<<<<<<<<<<<<<<<<<<<
 * ```
 * Second line:
 * ```
 * L898902C36UTO7408122F1204159<<<<<<<<<<<<<<00
 * ```
 *
 * ### Explanation of the example:
 *
 * 1. **P**: Document type (Passport)
 * 2. **UTO**: Issuing country code (Utopia)
 * 3. **ERIKSSON<<ANNA<MARIA**: Surname (ERIKSSON), Given name (ANNA MARIA)
 * 4. **L898902C3**: Passport number
 * 5. **6**: Check digit for passport number
 * 6. **UTO**: Nationality (Utopia)
 * 7. **740812**: Date of birth (12 August 1974)
 * 8. **2**: Check digit for date of birth
 * 9. **F**: Gender (Female)
 * 10. **120415**: Expiration date (15 April 2012)
 * 11. **9**: Check digit for expiration date
 * 12. **<<<<<<<<<<<<<<00**: Personal identifier and overall check character
 */
sealed class MRZResult {
    data class Success(val data: DataDocument) : MRZResult()
    data object Failure : MRZResult()
}

class MRZRecognitionOCR {
    private val documentTypeIdentifier = DocumentTypeIdentifier()


    //TODO MOVE ALL OF THIS IN THE TYPE IDENTIFIER - from here
    private val DOCUMENT_NUMBER = "documentNumber"
    private val DIGIT_DOCUMENT_NUMBER = "checkDigitDocumentNumber"
    private val BIRTH_DATE = "dateOfBirth"
    private val CHECK_BIRTH_DATE = "checkDigitDateOfBirth"
    private val EXPIRATION_DATE = "expirationDate"
    private val CHECK_EXPIRATION_DATE = "checkDigitExpirationDate"
    private val SEX = "sex"
    private val ISSUING_COUNTRY = "issuingCountry"
    private val NATIONALITY = "nationality"
    private val PERSONAL_IDENTIFIER = "personalIdentifier"
    private val LAST_NAME = "lastName"
    private val FIRST_NAME = "firstName" //not used on purpose,impossible to get full first name

    private val REGEX_PASSPORT_FIRST_LINE =
        "P<(?<$ISSUING_COUNTRY>[A-Z<]{3})(?<$LAST_NAME>[A-Z]{2,})<<([A-Z]+)"
    private val REGEX_OLD_PASSPORT =
        "(?<$DOCUMENT_NUMBER>[A-Z0-9<]{9})(?<$DIGIT_DOCUMENT_NUMBER>[0-9ILDSOG]{1})(?<nationality>[A-Z<]{3})(?<$BIRTH_DATE>[0-9ILDSOG]{6})(?<$CHECK_BIRTH_DATE>[0-9ILDSOG]{1})(?<sex>[FM<]){1}(?<$EXPIRATION_DATE>[0-9ILDSOG]{6})(?<$CHECK_EXPIRATION_DATE>[0-9ILDSOG]{1})"

    private val REGEX_NEW_CARD_LINE_1 =
        "ID(?<$ISSUING_COUNTRY>[A-Z<]{3})(?<$DOCUMENT_NUMBER>[A-Z0-9<]{9})(?<$DIGIT_DOCUMENT_NUMBER>[0-9]{1})"
    private val REGEX_NEW_CARD_LINE_2 =
        "(?<$BIRTH_DATE>[0-9]{6})(?<$CHECK_BIRTH_DATE>[0-9]{1})(?<$SEX>[FM]{1})(?<$EXPIRATION_DATE>[0-9]{6})(?<$CHECK_EXPIRATION_DATE>[0-9]{1})(?<$NATIONALITY>[A-Z<]{3})"

    private val REGEX_NEW_CARD_LINE_3 = "(?<$LAST_NAME>[A-Z]{2,})<<([A-Z]+)"

    //to here

    fun recognize(blocks: List<Text.TextBlock>): MRZResult {
        val fullRead = blocks.joinToString("-") { block ->
            block.lines.joinToString("-") { line ->
                line.text.trim()
                    .replace("\r", "")
                    .replace("\n", "")
                    .replace("\t", "")
                    .replace(" ", "")
            }
        }.uppercase(Locale.getDefault())

        val oldPassportMatcher = Pattern.compile(REGEX_OLD_PASSPORT).matcher(fullRead)
        val firstLinePassportMatcher = Pattern.compile(REGEX_PASSPORT_FIRST_LINE).matcher(fullRead)
        //todo vu que le text fullRead est une ligne entiere, sans retour a la ligne, est ce qu'il faudrait pas avoir un seul regex ?
        val newIdentityCard = Pattern.compile(REGEX_NEW_CARD_LINE_1).matcher(fullRead)
        val newIdentityCard2 = Pattern.compile(REGEX_NEW_CARD_LINE_2).matcher(fullRead)
        val newIdentityCard3 = Pattern.compile(REGEX_NEW_CARD_LINE_3).matcher(fullRead)

        return when {
            newIdentityCard.find() && newIdentityCard2.find() && newIdentityCard3.find() -> processIdentityCard(
                newIdentityCard,
                newIdentityCard2,
                newIdentityCard3
            )

            oldPassportMatcher.find() && firstLinePassportMatcher.find() -> processPassport(
                firstLinePassportMatcher,
                oldPassportMatcher
            )
            else -> MRZResult.Failure
        }
    }

    private fun processPassport(matcher1: Matcher, matcher2: Matcher): MRZResult {
        return processDocument(
            type = DocumentType.PASSPORT,
            issuingCountry = matcher1.group(ISSUING_COUNTRY),
            documentNumber = matcher2.group(DOCUMENT_NUMBER),
            checkDigitDocumentNumber = matcher2.group(DIGIT_DOCUMENT_NUMBER),
            dateOfBirth = matcher2.group(BIRTH_DATE),
            sex = matcher2.group(SEX),
            nationality = matcher2.group(NATIONALITY),
            lastName = matcher1.group(LAST_NAME),
            expirationDate = matcher2.group(EXPIRATION_DATE)
        )
    }

    private fun processIdentityCard(
        matcher1: Matcher,
        matcher2: Matcher,
        matcher3: Matcher
    ): MRZResult {
        return processDocument(
            type = DocumentType.ID_CARD,
            issuingCountry = matcher1.group(ISSUING_COUNTRY),
            documentNumber = matcher1.group(DOCUMENT_NUMBER),
            checkDigitDocumentNumber = matcher1.group(DIGIT_DOCUMENT_NUMBER),
            lastName = matcher3.group(LAST_NAME),
            dateOfBirth = matcher2.group(BIRTH_DATE),
            nationality = matcher2.group(NATIONALITY),
            sex = matcher2.group(SEX),
            expirationDate = matcher2.group(EXPIRATION_DATE)
        )
    }

    private fun processDocument(
        type: DocumentType,
        issuingCountry: String,
        documentNumber: String,
        lastName: String,
        checkDigitDocumentNumber: String,
        dateOfBirth: String,
        nationality: String,
        sex: String,
        expirationDate: String
    ): MRZResult {
        val result = cleanDocumentNumber(
            documentNumber = documentNumber,
            checkDigit = cleanDigit(checkDigitDocumentNumber).toInt()
        )

        return when (result) {
            is Result.Error -> MRZResult.Failure
            is Result.Success -> MRZResult.Success(
                DataDocument(
                    type = type,
                    issuingCountry = issuingCountry,
                    documentNumber = result.data,
                    nationality = nationality,
                    lastName = cleanCharacter(lastName),
                    dateOfBirth = cleanDigit(dateOfBirth),
                    dateOfExpiry = cleanDigit(expirationDate),
                    sex = sex
                )
            )
        }
    }

    private fun cleanDocumentNumber(
        documentNumber: String,
        checkDigit: Int
    ): Result<String, DataError.Local> {
        // Replace all 'O' with '0'
        val tempDocumentNumber = documentNumber.replace("O", "0")

        // Calculate check digit of the document number
        var calculatedCheckDigit = checkDigit(tempDocumentNumber)

        // If check digits match, return the document number
        if (checkDigit == calculatedCheckDigit) {
            return Result.Success(tempDocumentNumber)
        }

        // Try replacing each '0' with 'O' one at a time to see if it matches the check digit
        tempDocumentNumber.forEachIndexed { index, char ->
            if (char == '0') {
                val modifiedDocumentNumber = tempDocumentNumber.replaceRange(index, index + 1, "O")
                calculatedCheckDigit = checkDigit(modifiedDocumentNumber)
                if (checkDigit == calculatedCheckDigit) {
                    return Result.Success(modifiedDocumentNumber)
                }
            }
        }

        // If no match is found, return null
        return Result.Error(DataError.Local.INVALID_DATA)
    }

    /**
     * The check digit for the passport number is a digit calculated from the characters of the passport number, used to verify the accuracy and integrity of the data. This check digit is generated by applying a checksum algorithm (modulo 10) to the characters of the passport number.
     *
     * ### Calculation of the check digit
     * The calculation is done in the following steps:
     * 1. **Assignment of numerical values to characters**:
     *    - Digits from 0 to 9 retain their numerical values (e.g., 0 → 0, 1 → 1, ..., 9 → 9).
     *    - Letters from A to Z are converted to numerical values: A = 10, B = 11, ..., Z = 35.
     * 2. **Multiplication by weights**:
     *    - Each character is multiplied by a cyclic weight of 7, 3, and 1. These weights are applied repetitively.
     * 3. **Sum of results**:
     *    - The obtained products are summed.
     * 4. **Calculation of modulo 10**:
     *    - The total sum is divided by 10, and the remainder of this division is the check digit.
     *
     * ### Detailed Example
     * Let's take an example with the passport number: **L898902C3**.
     * 1. **Conversion of characters to numerical values**:
     *    - L = 21
     *    - 8 = 8
     *    - 9 = 9
     *    - 8 = 8
     *    - 9 = 9
     *    - 0 = 0
     *    - 2 = 2
     *    - C = 12
     *    - 3 = 3
     * 2. **Multiplication by weights (7, 3, 1)**:
     *    - 21 × 7 = 147
     *    - 8 × 3 = 24
     *    - 9 × 1 = 9
     *    - 8 × 7 = 56
     *    - 9 × 3 = 27
     *    - 0 × 1 = 0
     *    - 2 × 7 = 14
     *    - 12 × 3 = 36
     *    - 3 × 1 = 3
     * 3. **Sum of products**:
     *    - 147 + 24 + 9 + 56 + 27 + 0 + 14 + 36 + 3 = 316
     * 4. **Calculation of modulo 10**:
     *    - 316 % 10 = 6
     * So, the check digit for the passport number **L898902C3** is **6**.
     * This check digit is then placed at the next position of the passport number in the Machine Readable Zone (MRZ) to verify the integrity of the data during machine reading.
     */
    private fun checkDigit(documentNumber: String): Int {
        require(documentNumber.length == 9) { "Document number must be 9 characters long" }

        val weights = intArrayOf(7, 3, 1)
        var sum = 0

        for (i in documentNumber.indices) {
            val char = documentNumber[i]
            val value = if (char.isLetter()) {
                // Convert letters to corresponding numerical values (A = 10, B = 11, ..., Z = 35)
                char.uppercaseChar() - 'A' + 10
            } else {
                // Convert digits to numerical values
                char - '0'
            }
            // Multiply by the appropriate weight (7, 3, 1)
            sum += value * weights[i % 3]
        }
        // Return the remainder of the sum divided by 10
        return sum % 10
    }

    private fun cleanDigit(digit: String): String {
        return digit.map { char ->
            when (char) {
                'I', 'L' -> '1'
                'D', 'O' -> '0'
                'S' -> '5'
                'G' -> '6'
                else -> char
            }
        }.joinToString("")
    }

    private fun cleanCharacter(digit: String): String {
        return digit.map { char ->
            when (char) {
                '1' -> "I"
                '0' -> "O"
                '5' -> "S"
                '6' -> "G"
                else -> char.toString() // If the character is not a digit, keep it unchanged
            }
        }.joinToString("")
    }
}